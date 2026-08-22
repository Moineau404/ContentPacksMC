package mod.moineau.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;

public class DecodingOnlyAlternativeCodec<A> implements Codec<A> {
    private final Codec<A> first;
    private final Decoder<A> second;

    public DecodingOnlyAlternativeCodec(Codec<A> first, Decoder<A> second) {
        this.first = first;
        this.second = second;
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
        return first.encode(input, ops, prefix);
    }
}
