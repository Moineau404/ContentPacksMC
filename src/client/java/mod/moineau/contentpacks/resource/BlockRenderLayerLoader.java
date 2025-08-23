package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.codec.VanillaClientCodecs;
import mod.moineau.contentpacks.block.BlockStateDefinition;
import mod.moineau.contentpacks.mixin.client.BlockStateManagersAccessor;
import mod.moineau.contentpacks.render.block.DynamicBlockRenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

// TODO Maybe find a way to inject it directly in model variants.
public class BlockRenderLayerLoader implements ResourceReloader {
    private static final ResourceFinder FINDER = ResourceFinder.json("blockstates");
    private static final Codec<Optional<BlockRenderLayer>> CODEC = VanillaClientCodecs.BLOCK_RENDER_LAYER.optionalFieldOf("render").codec();

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        Function<Identifier, StateManager<Block, BlockState>> id2Manager = BlockStateManagersAccessor.createIdToManagerMapper();

        return CompletableFuture.supplyAsync(
                () -> FINDER.findResources(manager),
                prepareExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<BlockStateDefinition.Baked<BlockRenderLayer>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier identifier = FINDER.toResourceId(entry.getKey());
                    StateManager<Block, BlockState> stateManager = id2Manager.apply(identifier);

                    if (stateManager == null) {
                        ContentPacks.LOGGER.debug("Discovered unknown block render layer definition {}, ignoring", identifier);
                        return null;
                    } else {
                        Resource resource = entry.getValue();

                        try {
                            Reader reader = resource.getReader();

                            try {
                                JsonElement jsonElement = StrictJsonParser.parse(reader);
                                Optional<BlockRenderLayer> layer = CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);

                                if (reader != null) {
                                    reader.close();
                                }

                                return layer.map(BlockStateDefinition::unit).map(value -> value.bake(stateManager)).orElse(null);
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
                            ContentPacks.LOGGER.error("Failed to load block render layer definition {} from pack {}", identifier, resource.getPackId(), var14);
                            return null;
                        }
                    }
                }, prepareExecutor));
            }

            return Util.combineSafe(list).thenAcceptAsync(definitions -> {
                DynamicBlockRenderLayers.clear();

                for (BlockStateDefinition.Baked<BlockRenderLayer> definition : definitions) {
                    if (definition != null) {
                        definition.forEach(DynamicBlockRenderLayers::putBlock);
                    }
                }
            });
        }).thenCompose(synchronizer::whenPrepared);
    }
}