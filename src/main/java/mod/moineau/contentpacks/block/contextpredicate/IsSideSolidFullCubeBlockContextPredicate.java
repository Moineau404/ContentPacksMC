package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public record IsSideSolidFullCubeBlockContextPredicate(Direction direction) implements BlockContextPredicate {
    public static final MapCodec<IsSideSolidFullCubeBlockContextPredicate> CODEC = Direction.CODEC.fieldOf("direction")
            .xmap(IsSideSolidFullCubeBlockContextPredicate::new, IsSideSolidFullCubeBlockContextPredicate::direction);

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos) {
        return state.isSideSolidFullSquare(world, pos, direction);
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.IS_SIDE_SOLID_FULL_CUBE;
    }
}
