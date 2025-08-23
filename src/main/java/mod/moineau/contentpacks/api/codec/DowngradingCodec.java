package mod.moineau.contentpacks.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

/**
 * Workaround to allow encoding of fucntions/predicates etc... if they are serializable, and ignore if they are not.
 */
@ApiStatus.Internal
public final class DowngradingCodec<S, O extends S> implements Codec<S> {
    private final Codec<O> codec;
    private final boolean lenient;

    public DowngradingCodec(Codec<O> codec, boolean lenient) {
        this.codec = codec;
        this.lenient = lenient;
    }

    public DowngradingCodec(Codec<O> codec) {
        this(codec, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
        try {
            return codec.encode((O) input, ops, prefix);
        } catch (ClassCastException e) {
            if (lenient) {
                return DataResult.success(prefix);
            }
            return DataResult.error(() -> "Failed upgrading value", prefix);
        }
    }

    @Override
    public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
        return codec.decode(ops, input)
                .map(pair -> pair.mapFirst(Function.identity()));
    }
}
