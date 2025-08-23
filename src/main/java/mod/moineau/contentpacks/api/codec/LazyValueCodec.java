package mod.moineau.contentpacks.api.codec;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A codec that builds a memoizing type O supplier from type A object and the converter.
 * In other words, it's a lazy value in form of a supplier.
 * @param <O> Type of the value
 * @param <A> Type of the transient value used to build the final value
 * @deprecated Use {@link mod.moineau.contentpacks.api.util.CodecUtil#lazy(Decoder, Encoder, Function) CodecUtil.lazy}
 */
@Deprecated
public final class LazyValueCodec<O, A> implements Codec<Supplier<O>> {
    private final Decoder<A> decoder;
    private final Encoder<O> encoder;
    private final Function<A, O> converter;

    public LazyValueCodec(Decoder<A> decoder, Encoder<O> encoder, Function<A, O> converter) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.converter = converter;
    }

    @Override
    public <T> DataResult<T> encode(Supplier<O> input, DynamicOps<T> ops, T prefix) {
        return encoder.encode(input.get(), ops, prefix);
    }

    @Override
    public <T> DataResult<Pair<Supplier<O>, T>> decode(DynamicOps<T> ops, T input) {
        return decoder.decode(ops, input).map(pair -> pair.mapFirst(a -> Suppliers.memoize(() -> converter.apply(a))));
    }
}
