package mod.moineau.contentpacks.mixin.block;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin {
//    @Shadow
//    @Final
//    protected StateManager<Block, BlockState> stateManager;
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void inject$init(AbstractBlock.Settings settings, CallbackInfo ci) {
//        Bakeable.bake(stateManager, settings.allowsSpawningPredicate, settings.solidBlockPredicate,
//                settings.suffocationPredicate, settings.blockVisionPredicate, settings.postProcessPredicate,
//                settings.emissiveLightingPredicate, settings.mapColorProvider, settings.luminance).getOrThrow();
//    }
}