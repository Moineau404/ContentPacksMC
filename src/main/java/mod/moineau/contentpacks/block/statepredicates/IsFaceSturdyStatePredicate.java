package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated
public record IsFaceSturdyStatePredicate(Direction direction) implements StatePredicate {
    public static final MapCodec<IsFaceSturdyStatePredicate> CODEC = Direction.CODEC.fieldOf("direction")
            .xmap(IsFaceSturdyStatePredicate::new, IsFaceSturdyStatePredicate::direction);

    @Override
    public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
        return state.isFaceSturdy(world, pos, direction);
    }

    @Override
    public StatePredicateType<?> getType() {
        return StatePredicateType.IS_FACE_STURDY;
    }
}
