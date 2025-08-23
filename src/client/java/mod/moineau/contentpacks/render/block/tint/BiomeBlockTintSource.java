package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.world.biome.ColorResolvers;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.ColorResolver;

public class BiomeBlockTintSource extends DefaultedBlockTintSource {
    public static final MapCodec<BiomeBlockTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> fillDefaultedBlockTintSourceFields(instance).and(
                    ColorResolvers.CODEC.fieldOf("resolver").forGetter(BiomeBlockTintSource::getResolver)
            ).apply(instance, BiomeBlockTintSource::new));

    private final ColorResolver resolver;

    public BiomeBlockTintSource(BlockTintSource defaultTintSource, ColorResolver resolver) {
        super(defaultTintSource);
        this.resolver = resolver;
    }

    @Override
    public int getValidatedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
        return world.getColor(pos, this.resolver);
    }

    @Override
    public MapCodec<BiomeBlockTintSource> getCodec() {
        return CODEC;
    }

    public ColorResolver getResolver() {
        return resolver;
    }
}
