package mod.moineau.contentpacks.client.resource;

import mod.moineau.contentpacks.client.ContentPacksClient;
import mod.moineau.contentpacks.client.codec.ClientCodecs;
import mod.moineau.contentpacks.client.mixin.LiquidBlockAccessor;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jspecify.annotations.Nullable;

public class FluidModelReloadListener extends BoundReloadListener<Fluid, FluidModel.Unbaked> {
    private static final String DIRECTORY = "models/fluid";

    public FluidModelReloadListener() {
        super(DIRECTORY, ClientCodecs.FLUID_MODEL);
    }

    @Override
    protected @Nullable Fluid getBound(Identifier id) {
        Block block = BuiltInRegistries.BLOCK.getValue(id);
        if (block != null && block instanceof LiquidBlock liquidBlock) {
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
    protected void nullErrorProvider(Identifier id) {
        ContentPacksClient.LOGGER.debug("Discovered unknown fluid model {}, ignoring", id);
    }

    @Override
    protected void readingErrorProvider(Identifier id, String pack, String message) {
        ContentPacksClient.LOGGER.error("Failed to load fluid model for fluid {} from pack {}: {}", id, pack, message);
    }
}