package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public final class IsCollisionShapeFullBlockStatePredicate implements StatePredicate {
    public static final IsCollisionShapeFullBlockStatePredicate INSTANCE = new IsCollisionShapeFullBlockStatePredicate();
    public static final MapCodec<IsCollisionShapeFullBlockStatePredicate> CODEC = MapCodec.unit(INSTANCE);

    private IsCollisionShapeFullBlockStatePredicate() {
    }

    @Override
    public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
        return state.isCollisionShapeFullBlock(world, pos);
    }

    @Override
    public StatePredicateType<?> getType() {
        return StatePredicateType.IS_COLLISION_SHAPE_FULL_BLOCK;
    }
}
