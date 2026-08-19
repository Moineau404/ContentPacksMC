package mod.moineau.contentpacks.mixin.item;

import mod.moineau.contentpacks.block.StrippableBlockStateMap;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
    public static Map<Block, Block> STRIPPABLES;

    @Unique
    private static final Map<BlockState, BlockState> STRIPPABLE_STATES = new HashMap<>();

    @Inject(method = "<clinit>", at = @At(value = "RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        StrippableBlockStateMap.setup(STRIPPABLE_STATES::put, STRIPPABLE_STATES::get, STRIPPABLE_STATES::clear);
    }

    @Inject(method = "getStripped", at = @At(value = "HEAD"), cancellable = true)
    public void inject$getStripped(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
        BlockState stripped = STRIPPABLE_STATES.get(state);
        if (stripped != null) {
            cir.setReturnValue(Optional.of(stripped));
        }
    }
}
