package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;

public final class BlocksMovementBlockStatePredicate implements BlockStatePredicate {
    public static final BlocksMovementBlockStatePredicate INSTANCE = new BlocksMovementBlockStatePredicate();
    public static final MapCodec<BlocksMovementBlockStatePredicate> CODEC = MapCodec.unit(INSTANCE);

    private BlocksMovementBlockStatePredicate() {}

    @Override
    public boolean test(BlockState state) {
        return state.blocksMovement();
    }

    @Override
    public BlockStatePredicateType<?> getType() {
        return BlockStatePredicateType.BLOCKS_MOVEMENT;
    }
}
