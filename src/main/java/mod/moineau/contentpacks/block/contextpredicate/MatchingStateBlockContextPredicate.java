package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.Bakeable;
import mod.moineau.contentpacks.block.statepredicate.BlockStatePredicate;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public record MatchingStateBlockContextPredicate(BlockStatePredicate predicate) implements BlockContextPredicate {
    public static final MapCodec<MatchingStateBlockContextPredicate> CODEC = BlockStatePredicate.BASE_CODEC.fieldOf("predicate")
            .xmap(MatchingStateBlockContextPredicate::new, MatchingStateBlockContextPredicate::predicate);

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos) {
        return predicate.test(state);
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.MATCHING_STATE;
    }

    @Override
    public DataResult<?> contentpacks$bake(StateManager<?, ?> stateManager) {
        return Bakeable.bake(predicate, stateManager);
    }
}