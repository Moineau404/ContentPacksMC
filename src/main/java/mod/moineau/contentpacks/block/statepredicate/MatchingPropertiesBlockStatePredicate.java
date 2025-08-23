package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.CachingBlockPropertiesPredicate;
import net.minecraft.block.BlockState;

public record MatchingPropertiesBlockStatePredicate(CachingBlockPropertiesPredicate predicate) implements BlockStatePredicate {
    public static final MapCodec<MatchingPropertiesBlockStatePredicate> CODEC = CachingBlockPropertiesPredicate.CODEC.fieldOf("properties")
            .xmap(MatchingPropertiesBlockStatePredicate::new, MatchingPropertiesBlockStatePredicate::predicate);

    @Override
    public boolean test(BlockState state) {
        return predicate.test(state);
    }

    @Override
    public BlockStatePredicateType<?> getType() {
        return null;
    }
}
