package mod.moineau.contentpacks.api.util;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.api.codec.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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

    public static <T> Codec<T> withAlternative(Codec<T> first, Codec<T> second, Predicate<T> firstCondition) {
        return new AlternativeCodec<>(first, second, firstCondition);
    }

    public static <T> Codec<T> withAlternative(Codec<T> first, Codec<T> second) {
        return new AlternativeCodec<>(first, second);
    }

    public static <T> MapCodec<T> mapWithAlternative(MapCodec<T> first, MapCodec<T> second, Predicate<T> firstCondition) {
        return new AlternativeMapCodec<>(first, second, firstCondition);
    }

    public static <T> MapCodec<T> mapWithAlternative(MapCodec<T> first, MapCodec<T> second) {
        return new AlternativeMapCodec<>(first, second);
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

    public static <T> Codec<Supplier<T>> lazy(Registry<T> registry) {
        return lazy(ResourceKey.codec(registry.key()), registry.byNameCodec(), registry::getValue);
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

    public static <T> MapCodec<T> nullable(Codec<T> codec, String name) {
        return codec.optionalFieldOf(name).xmap(
                o -> o.orElse(null),
                Optional::ofNullable
        );
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