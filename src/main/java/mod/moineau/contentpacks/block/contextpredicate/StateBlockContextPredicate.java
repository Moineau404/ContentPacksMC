package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.statepredicate.BlockStatePredicate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public record StateBlockContextPredicate(BlockStatePredicate predicate) implements BlockContextPredicate {
    public static final MapCodec<StateBlockContextPredicate> CODEC = BlockStatePredicate.BASE_CODEC.fieldOf("predicate")
            .xmap(StateBlockContextPredicate::new, StateBlockContextPredicate::predicate);

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos) {
        return predicate.test(state);
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.STATE_PREDICATE;
    }
}
