package mod.moineau.contentpacks.api.codec;

import com.mojang.serialization.*;

import java.util.stream.Stream;

public class DecodingOnlyAlternativeMapCodec<A> extends MapCodec<A> {
    private final MapCodec<A> primary;
    private final MapDecoder<A> alternative;

    public DecodingOnlyAlternativeMapCodec(MapCodec<A> primary, MapDecoder<A> alternative) {
        this.primary = primary;
        this.alternative = alternative;
    }

    /**
     * Copied from {@link com.mojang.serialization.codecs.EitherCodec#decode(DynamicOps, Object)}
     */
    @Override
    public <T1> DataResult<A> decode(DynamicOps<T1> ops, MapLike<T1> input) {
        final DataResult<A> firstRead = primary.decode(ops, input);
        if (firstRead.isSuccess()) {
            return firstRead;
        }
        final DataResult<A> secondRead = alternative.decode(ops, input);
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
        return primary.encode(input, ops, prefix);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.concat(primary.keys(ops), alternative.keys(ops));
    }
}
