package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.mixin.client.FluidBlockAccessor;
import mod.moineau.contentpacks.render.block.DynamicBlockRenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// TODO Clean/update
public class BlockRenderLayerLoader implements ResourceReloader {
    private static final ResourceFinder FINDER = ResourceFinder.json("blockstates");
    private static final Codec<Optional<BlockRenderLayer>> CODEC = CodecUtil.enumByName(BlockRenderLayer.class).optionalFieldOf("render").codec();

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(
                () -> FINDER.findResources(manager),
                prepareExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Pair<Block, BlockRenderLayer>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier identifier = FINDER.toResourceId(entry.getKey());
                    Block block = Registries.BLOCK.get(identifier);

                    // TODO Clean + error tracking
                    if (block == null) {
                        ContentPacks.LOGGER.debug("Discovered unknown block render layer {}, ignoring", identifier);
                        return null;
                    } else {
                        Resource resource = entry.getValue();

                        try {
                            Reader reader = resource.getReader();

                            try {
                                JsonElement jsonElement = StrictJsonParser.parse(reader);
                                Optional<BlockRenderLayer> layerResult = CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);

                                if (reader != null) {
                                    reader.close();
                                }

                                return layerResult.map(layer -> new Pair<>(block, layer)).orElse(null);
                            } catch (Throwable var13) {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable var12) {
                                        var13.addSuppressed(var12);
                                    }
                                }

                                throw var13;
                            }
                        } catch (Exception var14) {
                            ContentPacks.LOGGER.error("Failed to load block render layer {} from pack {}", identifier, resource.getPackId(), var14);
                            return null;
                        }
                    }
                }, prepareExecutor));
            }

            return Util.combineSafe(list).thenAcceptAsync(entries -> {
                DynamicBlockRenderLayers.clear();

                for (Pair<Block, BlockRenderLayer> entry : entries) {
                    if (entry != null) {
                        var block = entry.getLeft();
                        var layer = entry.getRight();
                        DynamicBlockRenderLayers.putBlock(block, layer);
                        if (block instanceof FluidBlock fluidBlock) {
                            var fluid = ((FluidBlockAccessor) fluidBlock).getFluid();
                            DynamicBlockRenderLayers.putFluid(fluid.getStill(), layer);
                            DynamicBlockRenderLayers.putFluid(fluid.getFlowing(), layer);
                        }
                    }
                }
            });
        }).thenCompose(synchronizer::whenPrepared);
    }
}