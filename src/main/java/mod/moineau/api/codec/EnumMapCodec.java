package mod.moineau.api.codec;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EnumMapCodec<E extends Enum<E>, V> extends MapCodec<Map<E, V>> implements BaseMapCodec<E, V> {
    private final EnumCodec<E> keyCodec;
    private final Codec<V> elementCodec;
    private final Set<String> names;
    private final Keyable keys;
    private final boolean strict;

    public EnumMapCodec(Class<E> enumClass, Codec<V> elementCodec, boolean strict) {
        this.keyCodec = new EnumCodec<>(enumClass);
        this.elementCodec = elementCodec;
        this.names = Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
        this.keys = Keyable.forStrings(this.names::stream);
        this.strict = strict;
    }

    @Override
    public Codec<E> keyCodec() {
        return keyCodec;
    }

    @Override
    public Codec<V> elementCodec() {
        return elementCodec;
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return keys.keys(ops);
    }

    private DataResult<Map<E, V>> validate(Map<E, V> map) {
        if (!strict || map.size() == names.size()) {
            return DataResult.success(map);
        }
        String error = names.stream().filter(key -> !map.containsKey(key)).toString();
        return DataResult.error(() -> "Missing keys in enum sourceMap: " + error);
    }

    @Override
    public <T> DataResult<Map<E, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        return BaseMapCodec.super.decode(ops, input).flatMap(this::validate);
    }

    @Override
    public <T> RecordBuilder<T> encode(final Map<E, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        return BaseMapCodec.super.encode(input, ops, prefix);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EnumMapCodec<?, ?> that = (EnumMapCodec<?, ?>) o;
        return Objects.equals(keyCodec, that.keyCodec) && Objects.equals(elementCodec, that.elementCodec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyCodec, elementCodec);
    }

    @Override
    public String toString() {
        return "EnumMapCodec[" + keyCodec + " -> " + elementCodec + ']';
    }
}
