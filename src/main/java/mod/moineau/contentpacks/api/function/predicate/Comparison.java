package mod.moineau.contentpacks.api.function.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.Predicate;

public final class Comparison<T extends Comparable<T>> implements Predicate<T> {
    private final Comparator comparator;
    private final T comparable;

    public static <T extends Comparable<T>> Codec<Comparison<T>> createCodec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Comparator.NAME_CODEC.fieldOf("type").forGetter(predicate -> predicate.comparator),
                elementCodec.fieldOf("value").forGetter(predicate -> predicate.comparable)
        ).apply(instance, Comparison::new));
    }

    private Comparison(Comparator comparator, T value) {
        this.comparator = comparator;
        this.comparable = value;
    }

    @Override
    public boolean test(T value) {
        return this.comparator.compare(this.comparable, value);
    }

    @Override
    public String toString() {
        return this.comparator.toString() + this.comparable.toString();
    }

    public static <T extends Comparable<T>> Comparison<T> equal(T value) {
        return new Comparison<>(Comparator.EQUAL, value);
    }

    public static <T extends Comparable<T>> Comparison<T> notEqual(T value) {
        return new Comparison<>(Comparator.NOT_EQUAL, value);
    }

    public static <T extends Comparable<T>> Comparison<T> greater(T value) {
        return new Comparison<>(Comparator.GREATER, value);
    }

    public static <T extends Comparable<T>> Comparison<T> greaterOrEqual(T value) {
        return new Comparison<>(Comparator.GREATER_OR_EQUAL, value);
    }

    public static <T extends Comparable<T>> Comparison<T> less(T value) {
        return new Comparison<>(Comparator.LESS, value);
    }

    public static <T extends Comparable<T>> Comparison<T> lessOrEqual(T value) {
        return new Comparison<>(Comparator.LESS_OR_EQUAL, value);
    }
}
