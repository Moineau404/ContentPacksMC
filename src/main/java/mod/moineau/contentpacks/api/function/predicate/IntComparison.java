package mod.moineau.contentpacks.api.function.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.IntPredicate;

public record IntComparison(Comparator comparator, int y) implements IntPredicate {
    public static final MapCodec<IntComparison> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Comparator.CODEC.fieldOf("sign").forGetter(predicate -> predicate.comparator),
            Codec.INT.fieldOf("operand").forGetter(predicate -> predicate.y)
    ).apply(instance, IntComparison::new));

    @Override
    public boolean test(int x) {
        return comparator.compare(x, y);
    }
}
