package mod.moineau.contentpacks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.render.block.DynamicBlockRenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(RenderLayers.class)
abstract class RenderLayersMixin {
    @Unique
    private static final Map<Block, BlockRenderLayer> DYNAMIC_BLOCKS = new IdentityHashMap<>();

    @Unique
    private static final Map<Fluid, BlockRenderLayer> DYNAMIC_FLUIDS = new IdentityHashMap<>();

    @Inject(method = "<clinit>*", at = @At("RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        DynamicBlockRenderLayers.setup(
                DYNAMIC_BLOCKS::put,
                DYNAMIC_FLUIDS::put,
                RenderLayersMixin::contentpacks$clearLayers
        );
    }

    @ModifyVariable(method = "getBlockLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getBlockLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) BlockState state) {
       return contentpacks$getBlockLayer(state.getBlock(), blockRenderLayer);
    }

    @ModifyVariable(method = "getMovingBlockLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getMovingBlockLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) BlockState state) {
       return contentpacks$getBlockLayer(state.getBlock(), blockRenderLayer);
    }

    @ModifyVariable(method = "getFluidLayer", at = @At("STORE"))
    private static BlockRenderLayer injected$getFluidLayer(BlockRenderLayer blockRenderLayer, @Local(argsOnly = true) FluidState state) {
       return contentpacks$getFluidLayer(state.getFluid(), blockRenderLayer);
    }

    @Unique
    private static BlockRenderLayer contentpacks$getBlockLayer(Block block, BlockRenderLayer vanilla) {
        BlockRenderLayer blockRenderLayer = DYNAMIC_BLOCKS.get(block);
        return blockRenderLayer != null ? blockRenderLayer : vanilla;
    }

    @Unique
    private static BlockRenderLayer contentpacks$getFluidLayer(Fluid fluid, BlockRenderLayer vanilla) {
        BlockRenderLayer blockRenderLayer = DYNAMIC_FLUIDS.get(fluid);
        return blockRenderLayer != null ? blockRenderLayer : vanilla;
    }

    @Unique
    private static void contentpacks$clearLayers() {
        DYNAMIC_BLOCKS.clear();
        DYNAMIC_FLUIDS.clear();
    }
}