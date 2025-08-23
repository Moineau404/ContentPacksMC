package mod.moineau.contentpacks.api.util;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.codec.*;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CodecUtil {
    private static final Encoder<?> EMPTY = new Encoder<>() {
        @Override
        public <T1> DataResult<T1> encode(Object input, DynamicOps<T1> ops, T1 prefix) {
            return DataResult.success(prefix);
        }
    };

    public static <T> Codec<T> of(Decoder<T> decoder, Encoder<T> encoder) {
        return new Codec<>() {
            @Override
            public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
                return decoder.decode(ops, input);
            }

            @Override
            public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
                return encoder.encode(input, ops, prefix);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Encoder<T> empty() {
        return (Encoder<T>) EMPTY;
    }

    public static <O, S, T extends S> RecordCodecBuilder<O, S> unilateral(MapDecoder<T> decoder) {
        return MapCodec.<S>of(Encoder.empty(), decoder.map(Function.identity())).forGetter(o -> null);
    }

    public static <T> Codec<Optional<T>> intentionallyOptional(Codec<T> codec) {
        return new IntentionallyOptionalCodec<>(codec);
    }

    /**
     * A simple codec that builds a constant supplier (supplier of instance).
     */
    public static <O> Codec<Supplier<O>> supplier(Codec<O> elementCodec) {
        return elementCodec.xmap(Suppliers::ofInstance, Supplier::get);
    }

    /**
     * A codec that builds a memoizing type T supplier from type A object mapped by the converter.
     * In other words, it's a lazy value in form of a supplier, where the value is computed on first access.
     * @param <A> Type of the value
     * @param <T> Type of the transient value used to build the final value
     */
    public static <T, A> Codec<Supplier<T>> lazy(Decoder<A> decoder, Encoder<T> encoder, Function<A, T> converter) {
        return of(decoder.map(a -> Suppliers.memoize(() -> converter.apply(a))), encoder.comap(Supplier::get));
    }

    @Workaround
    public static <S, O extends S> Codec<S> downgrade(Codec<O> codec, boolean lenient) {
        return new DowngradingCodec<>(codec, lenient);
    }

    @Workaround
    public static <S, O extends S> Codec<S> downgrade(Codec<O> codec) {
        return new DowngradingCodec<>(codec);
    }

    @Workaround
    public static <O, E extends O> Codec<E> upgrade(Codec<O> codec) {
        return new UpgradingCodec<>(codec);
    }

    public static <A, E> MapCodec<A> dependent(MapCodec<A> codec, MapCodec<E> dependency, Function<A, E> splitter, BiFunction<A, E, A> combiner) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                codec.forGetter(Function.identity()),
                dependency.forGetter(splitter)
        ).apply(instance, combiner));
    }

    public static <E extends Enum<E>> Codec<E> enumByName(Class<E> enumClass) {
        return new EnumCodec<>(enumClass);
    }

    public static <E extends Enum<E>, V> MapCodec<Map<E, V>> enumMap(Class<E> enumClass, Codec<V> elementCodec, boolean strict) {
        return new EnumMapCodec<>(enumClass, elementCodec, strict);
    }

    public static <E extends Enum<E>, V> MapCodec<Map<E, V>> enumMap(Class<E> enumClass, Codec<V> elementCodec) {
        return new EnumMapCodec<>(enumClass, elementCodec, false);
    }

    //----------------------------------------------------------------
    // DATA VALIDATION

    public static <O, T> Function<O, DataResult<T>> validate(Function<O, T> function, Supplier<String> message) {
        return from -> {
            try {
                T to = function.apply(from);
                return validate(to);
            } catch (RuntimeException e) {
                return DataResult.error(message);
            }
        };
    }

    public static <O, T> Function<O, DataResult<T>> validate(Function<O, T> function) {
        return validate(function, () -> "Error serializing");
    }

    public static <T> DataResult<T> validate(T value) {
        if (value != null) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Error serializing");
    }

    public static <O, T> DataResult<T> fail(O object) {
        return DataResult.error(() -> "Not serializable");
    }

    public static <T> DataResult<T> validate(T value, Predicate<T> predicate) {
        if (predicate.test(value)) return DataResult.success(value);
        return DataResult.error(() -> "");
    }

    public static <T> DataResult<T> validate(T value, boolean condition) {
        if (condition) return DataResult.success(value);
        return DataResult.error(() -> "");
    }

    public static <T> DataResult<T> validate(Optional<T> value) {
        return value.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Error serializing"));
    }

    public static <O, T extends O> DataResult<O> validate(O object, Class<T> type) {
        if (type.isInstance(object)) return DataResult.success(object);
        return DataResult.error(() -> "Error serializing : wrong type");
    }

    //

    public static JsonElement jsonInjectId(JsonElement jsonElement, Identifier id) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.addProperty("$id", id.toString());
            return jsonObject;
        }
        return jsonElement;
    }
}