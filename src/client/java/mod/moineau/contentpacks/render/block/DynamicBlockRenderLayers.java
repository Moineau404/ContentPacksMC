package mod.moineau.contentpacks.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.fluid.FluidState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DynamicBlockRenderLayers {
    private static final Map<BlockState, BlockRenderLayer> BLOCKS = new HashMap<>();
    private static final Map<FluidState, BlockRenderLayer> FLUIDS = new HashMap<>();
    private static BiConsumer<BlockState, BlockRenderLayer> BLOCK_HANDLER = BLOCKS::put;
    private static BiConsumer<FluidState, BlockRenderLayer> FLUID_HANDLER = FLUIDS::put;
    private static Runnable CLEARER = () -> {};

    public static void putBlock(BlockState state, BlockRenderLayer layer) {
        BLOCK_HANDLER.accept(state, layer);
    }

    public static void putFluid(FluidState state, BlockRenderLayer layer) {
        FLUID_HANDLER.accept(state, layer);
    }

    public static void clear() {
        CLEARER.run();
    }

    public static void setup(BiConsumer<BlockState, BlockRenderLayer> vanillaBlockHandler, BiConsumer<FluidState, BlockRenderLayer> vanillaFluidHandler, Runnable vanillaClearer) {
        BLOCKS.forEach(vanillaBlockHandler);
        FLUIDS.forEach(vanillaFluidHandler);

        BLOCK_HANDLER = vanillaBlockHandler;
        FLUID_HANDLER = vanillaFluidHandler;
        CLEARER = vanillaClearer;
    }
}