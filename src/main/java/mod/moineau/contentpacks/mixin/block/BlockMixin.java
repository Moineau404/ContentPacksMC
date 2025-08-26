package mod.moineau.contentpacks.mixin.block;

import mod.moineau.contentpacks.block.Bakeable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Shadow
    @Final
    protected StateManager<Block, BlockState> stateManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void inject$init(AbstractBlock.Settings settings, CallbackInfo ci) {
        Bakeable.bake(stateManager, settings.allowsSpawningPredicate, settings.solidBlockPredicate,
                settings.suffocationPredicate, settings.blockVisionPredicate, settings.postProcessPredicate,
                settings.emissiveLightingPredicate, settings.mapColorProvider, settings.luminance).getOrThrow();
    }
}