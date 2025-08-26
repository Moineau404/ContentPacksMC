package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.state.PropertiesPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

public final class MatchingPropertiesBlockStatePredicate implements BlockStatePredicate {
    public static final MapCodec<MatchingPropertiesBlockStatePredicate> CODEC = PropertiesPredicate.CODEC.fieldOf("properties")
            .xmap(MatchingPropertiesBlockStatePredicate::new, predicate -> predicate.predicate);

    private PropertiesPredicate predicate;

    public MatchingPropertiesBlockStatePredicate(PropertiesPredicate unbaked) {
        this.predicate = unbaked;
    }

    @Override
    public boolean test(BlockState state) {
        return predicate.test(state);
    }

    @Override
    public BlockStatePredicateType<?> getType() {
        return BlockStatePredicateType.MATCHING_PROPERTIES;
    }

    @Override
    public DataResult<?> contentpacks$bake(StateManager<?, ?> stateManager) {
        if (this.predicate instanceof PropertiesPredicate.Unbaked unbaked) {
            return unbaked.bake(stateManager.getProperties()).ifSuccess(baked -> this.predicate = baked);
        }
        return EMPTY_RESULT;
    }
}
