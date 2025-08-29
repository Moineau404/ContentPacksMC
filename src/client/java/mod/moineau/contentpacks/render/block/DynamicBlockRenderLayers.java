package mod.moineau.contentpacks.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.fluid.Fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DynamicBlockRenderLayers {
    private static final Map<Block, BlockRenderLayer> BLOCKS = new HashMap<>();
    private static final Map<Fluid, BlockRenderLayer> FLUIDS = new HashMap<>();
    private static BiConsumer<Block, BlockRenderLayer> BLOCK_HANDLER = BLOCKS::put;
    private static BiConsumer<Fluid, BlockRenderLayer> FLUID_HANDLER = FLUIDS::put;
    private static Runnable CLEARER = () -> {};

    public static void putBlock(Block block, BlockRenderLayer layer) {
        BLOCK_HANDLER.accept(block, layer);
    }

    public static void putFluid(Fluid fluid, BlockRenderLayer layer) {
        FLUID_HANDLER.accept(fluid, layer);
    }

    public static void clear() {
        CLEARER.run();
    }

    public static void setup(BiConsumer<Block, BlockRenderLayer> vanillaBlockHandler, BiConsumer<Fluid, BlockRenderLayer> vanillaFluidHandler, Runnable vanillaClearer) {
        BLOCKS.forEach(vanillaBlockHandler);
        FLUIDS.forEach(vanillaFluidHandler);

        BLOCK_HANDLER = vanillaBlockHandler;
        FLUID_HANDLER = vanillaFluidHandler;
        CLEARER = vanillaClearer;
    }
}