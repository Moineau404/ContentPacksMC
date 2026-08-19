package mod.moineau.contentpacks.client.render.block.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.client.world.biome.ColorResolvers;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.state.BlockState;

public final class ResolverBlockTintSource extends BlockTintSource {
    public static final MapCodec<ResolverBlockTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> fillContentBlockTintSourceFields(instance)
                    .and(ColorResolvers.CODEC.fieldOf("resolver").forGetter(ResolverBlockTintSource::getResolver))
                    .and(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("fallback", -1).forGetter(ResolverBlockTintSource::getDefaultColor))
                    .apply(instance, ResolverBlockTintSource::new)
    );

    private final int defaultColor;
    private final ColorResolver resolver;

    public ResolverBlockTintSource(boolean colorParticle, ColorResolver resolver, int defaultColor) {
        super(colorParticle);
        this.resolver = resolver;
        this.defaultColor = defaultColor;
    }

    @Override
    public int color(BlockState state) {
        return defaultColor;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return level.getBlockTint(pos, this.resolver);
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public ColorResolver getResolver() {
        return resolver;
    }

    @Override
    public MapCodec<? extends BlockTintSource> getCodec() {
        return CODEC;
    }
}
