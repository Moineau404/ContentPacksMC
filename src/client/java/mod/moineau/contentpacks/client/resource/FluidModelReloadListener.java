package mod.moineau.contentpacks.client.resource;

import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.codec.ClientCodecs;
import mod.moineau.contentpacks.api.client.mixin.block.LiquidBlockAccessor;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jspecify.annotations.Nullable;

public final class FluidModelReloadListener extends DependentReloadListener<Fluid, FluidModel.Unbaked> {
    public FluidModelReloadListener() {
        super("models/fluid", ClientCodecs.FLUID_MODEL);
    }

    @Override
    protected @Nullable Fluid getDependence(Identifier id) {
        Block block = BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
        if (block instanceof LiquidBlock liquidBlock) {
            return ((LiquidBlockAccessor) liquidBlock).getFluid();
        }
        return null;
    }

    @Override
    protected void loadEntry(Fluid fluid, FluidModel.Unbaked model, Identifier id) {
        if (fluid instanceof FlowingFluid flowableFluid) {
            FluidRenderingRegistry.register(flowableFluid.getSource(), flowableFluid.getFlowing(), model);
        } else {
            FluidRenderingRegistry.register(fluid, model);
        }
    }

    @Override
    protected void handleNullError(Identifier id) {
        ContentPacksClient.LOGGER.debug("Discovered unknown fluid model {}, ignoring", id);
    }

    @Override
    protected void handleReadingError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Failed to load fluid model for fluid {} from pack {}: {}", id, pack, message);
    }

    @Override
    protected void handlePartialError(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Partially loaded fluid model for fluid {} from pack {}: {}", id, pack, message);
    }
}