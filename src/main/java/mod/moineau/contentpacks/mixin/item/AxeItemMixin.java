package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Allows {@link BlockState} -> {@link BlockState} mapping instead of just {@link Block} -> {@link Block}.
 * Vanilla behavior is keeped subjacent.
 */
@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Shadow
    @Final
    public static Map<Block, Block> STRIPPED_BLOCKS;

    @Unique
    private static final Map<BlockState, BlockState> STRIPPED_BLOCKSTATES = new HashMap<>();

    @Inject(method = "<clinit>", at = @At(value = "RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        StrippableBlockStateMap.setup(STRIPPED_BLOCKSTATES::put, STRIPPED_BLOCKSTATES::get, STRIPPED_BLOCKSTATES::clear);
    }

    @Inject(method = "getStrippedState", at = @At(value = "HEAD"), cancellable = true)
    public void inject$getStrippedState(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
        BlockState stripped = STRIPPED_BLOCKSTATES.get(state);
        if (stripped != null) {
            cir.setReturnValue(Optional.of(stripped));
        }
    }
}
