package mod.moineau.contentpacks.mapping;

import com.mojang.serialization.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@ApiStatus.Experimental
public final class MappedCodec<A> extends MapCodec<A> {
    private final Codec<A> elementCodec;
    private final Mappings.Key key;

    public MappedCodec(final Codec<A> elementCodec, final Mappings.Key key) {
        this.elementCodec = elementCodec;
        this.key = key;
    }

    public MappedCodec(final Codec<A> elementCodec, final String key) {
        this(elementCodec, Mappings.Key.of(key));
    }

    @Override
    public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final T value = key.get(input::get);
        if (value == null) {
            return DataResult.error(() -> "No key " + Arrays.toString(key.names().toArray()) + " in " + input);
        }
        return elementCodec.parse(ops, value);
    }

    @Override
    public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return prefix.add(key.name(), elementCodec.encodeStart(ops, input));
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return key.names().map(ops::createString);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MappedCodec<?> that = (MappedCodec<?>) o;
        return Objects.equals(key, that.key) && Objects.equals(elementCodec, that.elementCodec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, elementCodec);
    }

    @Override
    public String toString() {
        return "MappedCodec[(" + key + "): " + elementCodec + ']';
    }
}