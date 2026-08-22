package mod.moineau.api.util;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.codec.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CodecUtil {
    public static final String INJECT_ID_KEY = "#id";
    public static final String INJECT_LOCATION_KEY = "#location";

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

    public static <T> Codec<T> withDecodingOnlyAlternative(Codec<T> primary, Decoder<T> alternative) {
        return new DecodingOnlyAlternativeCodec<>(primary, alternative);
    }

    public static <T> MapCodec<T> mapWithDecodingOnlyAlternative(MapCodec<T> primary, MapDecoder<T> alternative) {
        return new DecodingOnlyAlternativeMapCodec<>(primary, alternative);
    }

    public static <O, S, T extends S> RecordCodecBuilder<O, S> unilateral(MapDecoder<T> decoder) {
        return MapCodec.<S>of(Encoder.empty(), decoder.map(Function.identity())).forGetter(o -> null);
    }

    public static <S, T extends S> MapCodec<S> unilateralMap(MapDecoder<T> decoder) {
        return MapCodec.of(Encoder.empty(), decoder.map(Function.identity()));
    }

    public static <T> MapCodec<T> optional(Codec<T> codec, String name, Supplier<T> defaultSupplier, boolean lenient) {
        return Codec.optionalField(name, codec, lenient).xmap(
                o -> o.orElseGet(defaultSupplier),
                a -> Objects.equals(a, defaultSupplier.get()) ? Optional.empty() : Optional.of(a)
        );
    }

    public static <T> MapCodec<T> optional(Codec<T> codec, String name, Supplier<T> defaultSupplier) {
        return optional(codec, name, defaultSupplier, false);
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
    public static <T, A> Codec<Supplier<T>> lazy(Encoder<T> encoder, Decoder<A> decoder, Function<A, T> converter) {
        return Codec.of(encoder.comap(Supplier::get), decoder.map(a -> Suppliers.memoize(() -> converter.apply(a))));
    }

    public static <T> Codec<Supplier<T>> lazy(Registry<T> registry) {
        return lazy(registry.byNameCodec(), ResourceKey.codec(registry.key()), registry::getValue);
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
            jsonObject.addProperty(INJECT_ID_KEY, id.toString());
            return jsonObject;
        }
        return jsonElement;
    }
}