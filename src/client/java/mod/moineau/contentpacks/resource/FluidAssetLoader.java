package mod.moineau.contentpacks.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.ContentPacks;
import mod.moineau.contentpacks.mixin.client.FluidBlockAccessor;
import mod.moineau.contentpacks.render.fluid.FluidAsset;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
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
public class FluidAssetLoader implements ResourceReloader {
    private static final ResourceFinder FINDER = ResourceFinder.json("blockstates");
    private static final Codec<Optional<FluidAsset>> CODEC = FluidAsset.CODEC.optionalFieldOf("fluid").codec();

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return CompletableFuture.supplyAsync(
                () -> FINDER.findResources(manager),
                prepareExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Pair<FlowableFluid, FluidAsset>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier identifier = FINDER.toResourceId(entry.getKey());
                    Block block = Registries.BLOCK.get(identifier);

                    // TODO Clean + error tracking
                    if (block instanceof FluidBlock fluidBlock) {
                        Resource resource = entry.getValue();

                        try {
                            Reader reader = resource.getReader();

                            try {
                                JsonElement jsonElement = StrictJsonParser.parse(reader);
                                Optional<FluidAsset> fluidAsset = CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);

                                if (reader != null) {
                                    reader.close();
                                }

                                return fluidAsset.map(tints -> new Pair<>(((FluidBlockAccessor) fluidBlock).getFluid(), tints)).orElse(null);
                            } catch (Throwable var13) {
                                ContentPacks.LOGGER.error("Failed to load fluid asset {} from pack {}", identifier, resource.getPackId(), var13);
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
                            ContentPacks.LOGGER.error("Failed to load fluid asset {} from pack {}", identifier, resource.getPackId(), var14);
                            return null;
                        }
                    } else {
                        ContentPacks.LOGGER.debug("Discovered unknown fluid asset {}, ignoring", identifier);
                        return null;
                    }
                }, prepareExecutor));
            }

            return Util.combineSafe(list).thenAcceptAsync(entries -> {
                for (Pair<FlowableFluid, FluidAsset> entry : entries) {
                    if (entry != null) {
                        entry.getRight().register(entry.getLeft()).getOrThrow();
                    }
                }
            });
        }).thenCompose(synchronizer::whenPrepared);
    }
}