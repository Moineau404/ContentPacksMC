package mod.moineau.contentpacks.api.codec;

import com.mojang.serialization.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

@Deprecated
public final class SimpleDependantCodec<O, E> extends MapCodec<O> {
    private final MapCodec<O> codec;
    private final MapCodec<E> dependency;
    private final Function<O, E> splitter;
    private final BiFunction<O, E, O> combiner;

    public SimpleDependantCodec(final MapCodec<O> codec, final MapCodec<E> dependency, final Function<O, E> splitter, final BiFunction<O, E, O> combiner) {
        this.codec = codec;
        this.dependency = dependency;
        this.splitter = splitter;
        this.combiner = combiner;
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return Stream.concat(codec.keys(ops), dependency.keys(ops));
    }

    @Override
    public <T> DataResult<O> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return codec.decode(ops, input).flatMap((O base) ->
                dependency.decode(ops, input).map(e -> combiner.apply(base, e)).setLifecycle(Lifecycle.experimental())
        );
    }

    @Override
    public <T> RecordBuilder<T> encode(final O input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        codec.encode(input, ops, prefix);
        final E e = splitter.apply(input);
        dependency.encode(e, ops, prefix);
        return prefix.setLifecycle(Lifecycle.experimental());
    }
}
