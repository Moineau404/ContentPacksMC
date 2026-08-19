package mod.moineau.contentpacks.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows {@link BlockState} -> {@link BlockState} mapping instead of just {@link Block} -> {@link BlockState}.
 * Vanilla behavior is keeped subjacent.
 */
@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Shadow
    @Final
    public static Map<Block, BlockState> FLATTENABLES;

    @Unique
    private static final Map<BlockState, BlockState> FLATTENABLE_STATES = new HashMap<>();

    @Inject(method = "<clinit>", at = @At(value = "RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        FlattenableBlockStateMap.setup(FLATTENABLE_STATES::put, FLATTENABLE_STATES::get, FLATTENABLE_STATES::clear);
    }

    @ModifyVariable(method = "useOn", at = @At(value = "STORE"), name = "newState")
    private static BlockState inject$useOn(BlockState newState, @Local(name = "blockState") BlockState blockState) {
        return contentpacks$getFlattened(blockState);
    }

    @Unique
    private static BlockState contentpacks$getFlattened(BlockState state) {
        BlockState flattened = FLATTENABLE_STATES.get(state);
        return flattened != null ? flattened : FLATTENABLES.get(state.getBlock());
    }
}
