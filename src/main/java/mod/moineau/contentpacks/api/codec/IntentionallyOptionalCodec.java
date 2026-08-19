package mod.moineau.contentpacks.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;

/**
 * Works only for string-based codecs!
 * Return an empty optional only if the base string value is empty.
 * If the base string is not empty, it decodes the value with the specified codec and makes it optional if success.
 * If the specified codec fails, it returns an error.
 * Example use : loot table in block settings, which can be intentionally empty (drops nothing).
 */
public record IntentionallyOptionalCodec<A>(Codec<A> codec) implements Codec<Optional<A>> {
    @Override
    public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getStringValue(input)
                .flatMap(string -> string.isEmpty()
                        ? DataResult.success(Pair.of(Optional.empty(), input))
                        : codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of)));
    }

    @Override
    public <T> DataResult<T> encode(Optional<A> input, DynamicOps<T> ops, T prefix) {
        if (input.isEmpty()) {
            return ops.mergeToPrimitive(prefix, ops.createString(""));
        }
        return codec.encode(input.get(), ops, prefix);
    }
}
