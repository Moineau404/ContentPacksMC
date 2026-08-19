package mod.moineau.contentpacks.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.Predicate;

/**
 * Simple alternative codec that works like {@link Codec#withAlternative(Codec, Codec)} except that
 * {@link AlternativeCodec#AlternativeCodec(Codec, Codec, Predicate) firstCondition} determines
 * which codec to use for encoding : if true, encodes with first, else encodes with second.
 */
public class AlternativeCodec<A> implements Codec<A> {
    private final Codec<A> first;
    private final Codec<A> second;
    private final Predicate<A> firstCondition;

    public AlternativeCodec(Codec<A> first, Codec<A> second, Predicate<A> firstCondition) {
        this.first = first;
        this.second = second;
        this.firstCondition = firstCondition;
    }

    public AlternativeCodec(Codec<A> first, Codec<A> second) {
        this(first, second, a -> true);
    }

    /**
     * Copied from {@link com.mojang.serialization.codecs.EitherCodec#decode(DynamicOps, Object)}
     */
    @Override
    public <T1> DataResult<Pair<A, T1>> decode(DynamicOps<T1> ops, T1 input) {
        final DataResult<Pair<A, T1>> firstRead = first.decode(ops, input);
        if (firstRead.isSuccess()) {
            return firstRead;
        }
        final DataResult<Pair<A, T1>> secondRead = second.decode(ops, input);
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
    public <T1> DataResult<T1> encode(A input, DynamicOps<T1> ops, T1 prefix) {
        if (firstCondition.test(input)) {
            return first.encode(input, ops, prefix);
        }
        return second.encode(input, ops, prefix);
    }
}
