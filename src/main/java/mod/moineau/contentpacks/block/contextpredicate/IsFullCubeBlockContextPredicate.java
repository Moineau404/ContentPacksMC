package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class IsFullCubeBlockContextPredicate implements BlockContextPredicate {
    public static final IsFullCubeBlockContextPredicate INSTANCE = new IsFullCubeBlockContextPredicate();
    public static final MapCodec<IsFullCubeBlockContextPredicate> CODEC = MapCodec.unit(INSTANCE);

    private IsFullCubeBlockContextPredicate() {
    }

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos) {
        return state.isFullCube(world, pos);
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.IS_FULL_CUBE;
    }
}
