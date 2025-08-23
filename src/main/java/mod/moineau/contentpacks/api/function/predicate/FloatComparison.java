package mod.moineau.contentpacks.api.function.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Predicate;

public record FloatComparison(Comparator comparator, float y) implements Predicate<Float> {
    public static final MapCodec<FloatComparison> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Comparator.CODEC.fieldOf("sign").forGetter(predicate -> predicate.comparator),
            Codec.FLOAT.fieldOf("operand").forGetter(predicate -> predicate.y)
    ).apply(instance, FloatComparison::new));

    @Override
    public boolean test(Float x) {
        return comparator.compare(x, y);
    }
}
