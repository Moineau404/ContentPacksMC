package mod.moineau.contentpacks.block;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Objects;
import java.util.function.ToIntFunction;

public sealed abstract class LightEmissionProvider implements ToIntFunction<BlockState> {
    public static final Codec<LightEmissionProvider> CODEC = Codec.either(
            Codec.intRange(0, 15).xmap(Constant::new, provider -> provider.value),
            VariantMap.createCodec(Codec.intRange(0, 15)).xmap(Variant::new, provider -> provider.variants)
    ).xmap(
            Either::unwrap,
            provider -> {
                if (provider instanceof Constant) {
                    return Either.left((Constant) provider);
                }
                if (provider instanceof Variant) {
                    return Either.right((Variant) provider);
                }
                throw new IllegalStateException();
            });
    public static final Codec<ToIntFunction<BlockState>> DOWNGRADED_CODEC = CodecUtil.downgrade(CODEC);

    public static LightEmissionProvider of(int value) {
        return new Constant(value);
    }

    public static LightEmissionProvider of(VariantMap<Integer> variants) {
        return new Variant(variants);
    }

    public static final class Constant extends LightEmissionProvider {
        private final int value;

        public Constant(int value) {
            this.value = value;
        }

        @Override
        public int applyAsInt(BlockState state) {
            return value;
        }
    }

    public static final class Variant extends LightEmissionProvider {
        private VariantMap<Integer> variants;

        private Variant(VariantMap<Integer> variants) {
            this.variants = variants;
        }

        @Override
        public int applyAsInt(BlockState state) {
            if (variants instanceof VariantMap.Unbaked<Integer> unbaked) {
                this.variants = unbaked.bake(state.getProperties()).getOrThrow();
            }
            return Objects.requireNonNullElse(variants.get(state), 0);
        }
    }
}
