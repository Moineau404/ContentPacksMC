package mod.moineau.contentpacks.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.render.block.DynamicBlockColors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.IdentityHashMap;
import java.util.Map;

//TODO Add compatibility with Sodium !!!
// - Stop using using custom map, use default IdList and make the blockstate -> provider thing directly in block tint source (or make it just normal block -> provider)
@Mixin(BlockColors.class)
public abstract class BlockColorsMixin implements DynamicBlockColors {
    @Unique
    private final Map<Block, BlockColorProvider> contentpacks$dynamicProviders = new IdentityHashMap<>();

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
        BlockColorProvider blockColorProvider = this.contentpacks$dynamicProviders.get(state.getBlock());
        return blockColorProvider != null ? blockColorProvider : vanilla;
    }

    @Unique
    @Override
    public void contentpacks$addProvider(Block block, BlockColorProvider provider) {
        this.contentpacks$dynamicProviders.put(block, provider);
    }

    @Unique
    @Override
    public void contentpacks$clearProviders() {
        this.contentpacks$dynamicProviders.clear();
    }
}
