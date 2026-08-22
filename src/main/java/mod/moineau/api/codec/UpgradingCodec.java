package mod.moineau.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class UpgradingCodec<O, E extends O> implements Codec<E> {
    private final Codec<O> codec;

    public UpgradingCodec(Codec<O> codec) {
        this.codec = codec;
    }

    @Override
    public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
        return codec.encode(input, ops, prefix);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
        try {
            return codec.decode(ops, input)
                    .map(pair -> pair.mapFirst(o -> (E) o));
        } catch (ClassCastException e) {
            return DataResult.error(() -> "Failed upgrading value");

        }
    }
}
