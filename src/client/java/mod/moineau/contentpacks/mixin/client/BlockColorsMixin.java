package mod.moineau.contentpacks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.render.block.DynamicBlockColorProviders;
import mod.moineau.contentpacks.render.block.DynamicBlockColors;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.Map;

//TODO Add compatibility with Sodium !!!
// - Stop using using custom map, use default IdList and make the blockstate -> provider thing directly in block tint source (or make it just normal block -> provider)
@Mixin(BlockColors.class)
public abstract class BlockColorsMixin implements DynamicBlockColors {
    @Unique
    private final Map<BlockState, BlockColorProvider> contentpacks$dynamicProviders = new IdentityHashMap<>();

    @Deprecated
    @Inject(method = "create", at = @At("RETURN"))
    private static void injected$create(CallbackInfoReturnable<BlockColors> info) {
        BlockColors blockColors = info.getReturnValue();
        DynamicBlockColorProviders.setup(
                blockColors,
                ((DynamicBlockColors) blockColors)::contentpacks$addProvider,
                ((DynamicBlockColors) blockColors)::contentpacks$clearProviders
        );
    }

    @ModifyVariable(method = "getParticleColor", at = @At("STORE"))
    private BlockColorProvider injected$getParticleColor(BlockColorProvider blockColorProvider, @Local(argsOnly = true) BlockState state) {
        return this.contentpacks$getProvider(state, blockColorProvider);
    }

    @ModifyVariable(method = "getColor", at = @At("STORE"))
    private BlockColorProvider injected$getColor(BlockColorProvider blockColorProvider, @Local(argsOnly = true) BlockState state) {
        return this.contentpacks$getProvider(state, blockColorProvider);
    }

    @Unique
    private BlockColorProvider contentpacks$getProvider(BlockState state, BlockColorProvider vanilla) {
        BlockColorProvider blockColorProvider = this.contentpacks$dynamicProviders.get(state);
        return blockColorProvider != null ? blockColorProvider : vanilla;
    }

    @Unique
    @Override
    public void contentpacks$addProvider(BlockState state, BlockColorProvider provider) {
        this.contentpacks$dynamicProviders.put(state, provider);
    }

    @Unique
    @Override
    public void contentpacks$clearProviders() {
        this.contentpacks$dynamicProviders.clear();
    }
}
