package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.render.block.DynamicBlockColors;
import mod.moineau.contentpacks.render.block.tint.BlockTints;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColors;
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
public class BlockColorLoader implements ResourceReloader {
    private static final ResourceFinder FINDER = ResourceFinder.json("blockstates");
    private static final Codec<Optional<BlockTints>> CODEC = BlockTints.CODEC.optionalFieldOf("tints").codec();

    private final BlockColors blockColors;

    public BlockColorLoader(BlockColors blockColors) {
        this.blockColors = blockColors;
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(
                () -> FINDER.findResources(manager),
                prepareExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Pair<Block, BlockTints>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier identifier = FINDER.toResourceId(entry.getKey());
                    Block block = Registries.BLOCK.get(identifier);

                    // TODO Clean + error tracking
                    if (block == null) {
                        ContentPacks.LOGGER.debug("Discovered unknown block color {}, ignoring", identifier);
                        return null;
                    } else {
                        Resource resource = entry.getValue();

                        try {
                            Reader reader = resource.getReader();

                            try {
                                JsonElement jsonElement = StrictJsonParser.parse(reader);
                                Optional<BlockTints> tintsResult = CODEC.parse(JsonOps.INSTANCE, jsonElement).getPartialOrThrow(JsonParseException::new);

                                if (reader != null) {
                                    reader.close();
                                }

                                return tintsResult.map(tints -> new Pair<>(block, tints)).orElse(null);
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
                            ContentPacks.LOGGER.error("Failed to load block color {} from pack {}", identifier, resource.getPackId(), var14);
                            return null;
                        }
                    }
                }, prepareExecutor));
            }

            return Util.combineSafe(list).thenAcceptAsync(entries -> {
                ((DynamicBlockColors) blockColors).contentpacks$clearProviders();

                for (Pair<Block, BlockTints> entry : entries) {
                    if (entry != null) {
                        ((DynamicBlockColors) blockColors).contentpacks$addProvider(entry.getLeft(), entry.getRight());
                    }
                }
            });
        }).thenCompose(synchronizer::whenPrepared);
    }
}