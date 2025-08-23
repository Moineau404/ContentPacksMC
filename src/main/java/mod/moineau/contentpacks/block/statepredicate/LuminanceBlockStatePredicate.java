package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.api.function.predicate.IntComparison;
import net.minecraft.block.BlockState;

public record LuminanceBlockStatePredicate(IntComparison predicate) implements BlockStatePredicate {
    public static final MapCodec<LuminanceBlockStatePredicate> CODEC = IntComparison.CODEC
            .xmap(LuminanceBlockStatePredicate::new, LuminanceBlockStatePredicate::predicate);

    @Override
    public boolean test(BlockState state) {
        return predicate.test(state.getLuminance());
    }

    @Override
    public BlockStatePredicateType<?> getType() {
        return BlockStatePredicateType.LUMINANCE;
    }
}
