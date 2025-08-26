package mod.moineau.contentpacks.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.apache.commons.lang3.EnumUtils;

public record EnumCodec<E extends Enum<E>>(Class<E> enumClass) implements Codec<E> {
    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getStringValue(input).flatMap(DataResult.partialGet(
                string -> EnumUtils.getEnumIgnoreCase(enumClass, string),
                () -> "No value in enum " + enumClass.getName() + " with name "
        )).map(r -> Pair.of(r, ops.empty()));
    }

    @Override
    public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
        return ops.mergeToPrimitive(prefix, ops.createString(input.name().toLowerCase()));
    }
}
