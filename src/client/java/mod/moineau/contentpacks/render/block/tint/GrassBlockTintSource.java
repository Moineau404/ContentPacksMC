package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.GrassColors;

@Environment(EnvType.CLIENT)
public record GrassBlockTintSource(float temperature, float downfall) implements BlockTintSource {
    public static final MapCodec<GrassBlockTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("temperature").forGetter(GrassBlockTintSource::temperature),
                    Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("downfall").forGetter(GrassBlockTintSource::downfall)
            ).apply(instance, GrassBlockTintSource::new)
    );

    @Override
    public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
        return GrassColors.getColor(this.temperature, this.downfall);
    }

    @Override
    public MapCodec<GrassBlockTintSource> getCodec() {
        return CODEC;
    }
}
