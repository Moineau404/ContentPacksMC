package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.api.function.predicate.Comparison;
import net.minecraft.block.BlockState;

public record LuminanceBlockStatePredicate(Comparison<Integer> predicate) implements BlockStatePredicate {
    public static final MapCodec<LuminanceBlockStatePredicate> CODEC = Comparison.createCodec(Codec.INT).fieldOf("comparison")
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
