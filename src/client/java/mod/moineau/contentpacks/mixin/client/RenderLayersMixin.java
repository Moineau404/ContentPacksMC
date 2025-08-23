package mod.moineau.contentpacks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.render.block.DynamicBlockRenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(RenderLayers.class)
abstract class RenderLayersMixin {
    @Shadow
    @Final
    private static Map<Block, BlockRenderLayer> BLOCKS;

    @Shadow
    @Final
    private static Map<Fluid, BlockRenderLayer> FLUIDS;

    @Unique
    private static final Map<BlockState, BlockRenderLayer> BLOCKSTATES = new IdentityHashMap<>();

    @Unique
    private static final Map<FluidState, BlockRenderLayer> FLUIDSTATES = new IdentityHashMap<>();

    @Inject(method = "<clinit>*", at = @At("RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        DynamicBlockRenderLayers.setup(
                RenderLayersMixin::contentpacks$addBlockLayer,
                RenderLayersMixin::contentpacks$addFluidLayer,
                RenderLayersMixin::contentpacks$clearLayers
        );
    }

    @ModifyVariable(method = "getBlockLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getBlockLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) BlockState state) {
       return contentpacks$getBlockLayer(state);
    }

    @ModifyVariable(method = "getMovingBlockLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getMovingBlockLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) BlockState state) {
       return contentpacks$getBlockLayer(state);
    }

    @ModifyVariable(method = "getFluidLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getFluidLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) FluidState state) {
       return contentpacks$getFluidLayer(state);
    }

    @Unique
    private static void contentpacks$addBlockLayer(BlockState state, BlockRenderLayer blockRenderLayer) {
        BLOCKSTATES.put(state, blockRenderLayer);
    }

    @Unique
    private static void contentpacks$addFluidLayer(FluidState state, BlockRenderLayer blockRenderLayer) {
        FLUIDSTATES.put(state, blockRenderLayer);
    }

    @Unique
    private static BlockRenderLayer contentpacks$getBlockLayer(BlockState state) {
        BlockRenderLayer blockRenderLayer = BLOCKSTATES.get(state);
        return blockRenderLayer != null ? blockRenderLayer : BLOCKS.get(state.getBlock());
    }

    @Unique
    private static BlockRenderLayer contentpacks$getFluidLayer(FluidState state) {
        BlockRenderLayer blockRenderLayer = FLUIDSTATES.get(state);
        return blockRenderLayer != null ? blockRenderLayer : FLUIDS.get(state.getFluid());
    }

    @Unique
    private static void contentpacks$clearLayers() {
        BLOCKSTATES.clear();
        FLUIDSTATES.clear();
    }
}