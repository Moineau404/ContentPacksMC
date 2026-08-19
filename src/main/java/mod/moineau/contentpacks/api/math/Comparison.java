package mod.moineau.contentpacks.api.math;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Function;
import java.util.function.Predicate;

public final class Comparison<T extends Comparable<T>> implements Predicate<T> {
    private final Comparator comparator;
    private final T comparable;

    public static <T extends Comparable<T>> Codec<Comparison<T>> createCodec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Comparator.NAME_CODEC.optionalFieldOf("is", Comparator.EQUAL_TO).forGetter(comparison -> comparison.comparator),
                elementCodec.fieldOf("value").forGetter(comparison -> comparison.comparable)
        ).apply(instance, Comparison::new));
    }

    public static <T extends Comparable<T>> Codec<Comparison<T>> createSimplifiedCodec(Codec<T> elementCodec) {
        return Codec.<T, Comparison<T>>either(
                        elementCodec,
                        RecordCodecBuilder.create(instance -> instance.group(
                                Comparator.NAME_CODEC.fieldOf("is").forGetter(comparison -> comparison.comparator),
                                elementCodec.fieldOf("value").forGetter(comparison -> comparison.comparable)
                        ).apply(instance, Comparison::new)))
                .xmap(
                        either -> either.map(Comparison::new, Function.identity()),
                        comparison -> {
                            if (comparison.comparator == Comparator.EQUAL_TO) {
                                return Either.left(comparison.comparable);
                            }
                            return Either.right(comparison);
                        });
    }

    private Comparison(Comparator comparator, T value) {
        this.comparator = comparator;
        this.comparable = value;
    }

    private Comparison(T value) {
        this(Comparator.EQUAL_TO, value);
    }

    @Override
    public boolean test(T value) {
        return this.comparator.compare(this.comparable, value);
    }

    @Override
    public String toString() {
        return this.comparator.toString() + this.comparable.toString();
    }

    public static <T extends Comparable<T>> Comparison<T> equalTo(T value) {
        return new Comparison<>(Comparator.EQUAL_TO, value);
    }

    public static <T extends Comparable<T>> Comparison<T> notEqualTo(T value) {
        return new Comparison<>(Comparator.NOT_EQUAL_TO, value);
    }

    public static <T extends Comparable<T>> Comparison<T> greaterThan(T value) {
        return new Comparison<>(Comparator.GREATER_THAN, value);
    }

    public static <T extends Comparable<T>> Comparison<T> greaterThanOrEqualTo(T value) {
        return new Comparison<>(Comparator.GREATER_THAN_OR_EQUAL_TO, value);
    }

    public static <T extends Comparable<T>> Comparison<T> lessThan(T value) {
        return new Comparison<>(Comparator.LESS_THAN, value);
    }

    public static <T extends Comparable<T>> Comparison<T> lessThanOrEqualTo(T value) {
        return new Comparison<>(Comparator.LESS_THAN_OR_EQUAL_TO, value);
    }
}
