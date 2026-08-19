package mod.moineau.contentpacks.api.codec;

import com.mojang.serialization.*;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Simple alternative map codec, {@link AlternativeMapCodec#AlternativeMapCodec(MapCodec, MapCodec, Predicate) firstCondition}
 * determines which map codec to use for encoding : if true, encodes with first, else encodes with second.
 */
public class AlternativeMapCodec<A> extends MapCodec<A> {
    private final MapCodec<A> first;
    private final MapCodec<A> second;
    private final Predicate<A> firstCondition;

    public AlternativeMapCodec(MapCodec<A> first, MapCodec<A> second, Predicate<A> firstCondition) {
        this.first = first;
        this.second = second;
        this.firstCondition = firstCondition;
    }

    public AlternativeMapCodec(MapCodec<A> first, MapCodec<A> second) {
        this(first, second, a -> true);
    }

    /**
     * Copied from {@link com.mojang.serialization.codecs.EitherCodec#decode(DynamicOps, Object)}
     */
    @Override
    public <T1> DataResult<A> decode(DynamicOps<T1> ops, MapLike<T1> input) {
        final DataResult<A> firstRead = first.decode(ops, input);
        if (firstRead.isSuccess()) {
            return firstRead;
        }
        final DataResult<A> secondRead = second.decode(ops, input);
        if (secondRead.isSuccess()) {
            return secondRead;
        }
        if (firstRead.hasResultOrPartial()) {
            return firstRead;
        }
        if (secondRead.hasResultOrPartial()) {
            return secondRead;
        }
        return DataResult.error(() -> "Failed to parse alternative. First: " + firstRead.error().orElseThrow().message() + "; Second: " + secondRead.error().orElseThrow().message());
    }

    @Override
    public <T1> RecordBuilder<T1> encode(A input, DynamicOps<T1> ops, RecordBuilder<T1> prefix) {
        if (firstCondition.test(input)) {
            return first.encode(input, ops, prefix);
        }
        return second.encode(input, ops, prefix);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.concat(first.keys(ops), second.keys(ops));
    }
}
