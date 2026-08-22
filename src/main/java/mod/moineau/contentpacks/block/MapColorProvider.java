package mod.moineau.contentpacks.block;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.contentpacks.state.VariantMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import java.util.Objects;
import java.util.function.Function;

public sealed abstract class MapColorProvider implements Function<BlockState, MapColor> {
    public static final Codec<MapColorProvider> CODEC = Codec.either(
            MapColors.CODEC.xmap(Constant::new, provider -> provider.color),
            VariantMap.createCodec(MapColors.CODEC).xmap(Variant::new, provider -> provider.variants)
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
    public static final Codec<Function<BlockState, MapColor>> DOWNGRADED_CODEC = CodecUtil.downgrade(CODEC);

    public static MapColorProvider of(MapColor color) {
        return new Constant(color);
    }

    public static MapColorProvider of(VariantMap<MapColor> variants) {
        return new Variant(variants);
    }

    public static final class Constant extends MapColorProvider {
        private final MapColor color;

        public Constant(MapColor color) {
            this.color = color;
        }

        @Override
        public MapColor apply(BlockState state) {
            return color;
        }
    }

    public static final class Variant extends MapColorProvider {
        private VariantMap<MapColor> variants;

        private Variant(VariantMap<MapColor> variants) {
            this.variants = variants;
        }

        @Override
        public MapColor apply(BlockState state) {
            if (variants instanceof VariantMap.Unbaked<MapColor> unbaked) {
                this.variants = unbaked.bake(state.getProperties()).getOrThrow();
            }
            return Objects.requireNonNullElse(variants.get(state), MapColor.NONE);
        }
    }
}
