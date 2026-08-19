package mod.moineau.contentpacks.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.codec.ClientCodecs;
import mod.moineau.contentpacks.client.mixin.LiquidBlockAccessor;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// TODO Clean/update
public class FluidModelLoader implements PreparableReloadListener {
    private static final FileToIdConverter FINDER = FileToIdConverter.json("models/fluid");

    private static void register(Fluid fluid, FluidModel.Unbaked model) {
        if (fluid instanceof FlowingFluid flowableFluid) {
            FluidRenderingRegistry.register(flowableFluid.getSource(), flowableFluid.getFlowing(), model);
        } else {
            FluidRenderingRegistry.register(fluid, model);
        }
    }

    @Override
    public CompletableFuture<Void> reload(
            SharedState currentReload,
            Executor taskExecutor,
            PreparationBarrier preparationBarrier,
            Executor reloadExecutor
    ) {
        ResourceManager manager = currentReload.resourceManager();
        return CompletableFuture.supplyAsync(
                () -> FINDER.listMatchingResources(manager),
                taskExecutor
        ).thenCompose(resourceMap -> {
            List<CompletableFuture<Pair<FlowingFluid, FluidModel.Unbaked>>> list = new ArrayList<>(resourceMap.size());

            for (Map.Entry<Identifier, Resource> entry : resourceMap.entrySet()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    Identifier id = FINDER.fileToId(entry.getKey());
                    Block block = BuiltInRegistries.BLOCK.getValue(id);

                    if (block instanceof LiquidBlock liquidBlock) {
                        Resource resource = entry.getValue();

                        try (Reader reader = resource.openAsReader()) {
                            JsonElement jsonElement = StrictJsonParser.parse(reader);
                            FluidModel.Unbaked model = ClientCodecs.FLUID_MODEL.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(JsonParseException::new);

                            return new Pair<>(((LiquidBlockAccessor) liquidBlock).getFluid(), model);
                        } catch (Exception e) {
                            ContentPacksClient.LOGGER.error("Failed to load fluid model for fluid {} from pack {}", id, resource.sourcePackId(), e);
                            return null;
                        }
                    } else {
                        ContentPacksClient.LOGGER.debug("Discovered unknown fluid model {}, ignoring", id);
                        return null;
                    }
                }, taskExecutor));
            }

            return Util.sequence(list).thenAcceptAsync(entries -> {
                for (Pair<FlowingFluid, FluidModel.Unbaked> entry : entries) {
                    if (entry != null) {
                        register(entry.getFirst(), entry.getSecond());
                    }
                }
            });
        }).thenCompose(preparationBarrier::wait);
    }
}