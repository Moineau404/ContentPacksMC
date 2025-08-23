package mod.moineau.contentpacks.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import mod.moineau.contentpacks.block.FlattenableBlockStateMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ShovelItem;
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
    public static Map<Block, BlockState> PATH_STATES;

    @Unique
    private static final Map<BlockState, BlockState> PATH_BLOCKSTATES = new HashMap<>();

    @Inject(method = "<clinit>", at = @At(value = "RETURN"))
    private static void inject$clinit(CallbackInfo info) {
        FlattenableBlockStateMap.setup(PATH_BLOCKSTATES::put, PATH_BLOCKSTATES::get, PATH_BLOCKSTATES::clear);
    }

    @ModifyVariable(method = "useOnBlock", at = @At(value = "STORE"))
    private static BlockState inject$useOnBlock(BlockState ignore, @Local BlockState state) {
        return contentpacks$getFlattenedState(state);
    }

    @Unique
    private static BlockState contentpacks$getFlattenedState(BlockState state) {
        BlockState flattened = PATH_BLOCKSTATES.get(state);
        return flattened != null ? flattened : PATH_STATES.get(state.getBlock());
    }
}
