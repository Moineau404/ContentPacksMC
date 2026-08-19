package mod.moineau.contentpacks.client.render.block.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

public class ConstantBlockTintSource extends BlockTintSource {
    public static final MapCodec<ConstantBlockTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> fillContentBlockTintSourceFields(instance)
                    .and(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("value").forGetter(ConstantBlockTintSource::getColor))
                    .apply(instance, ConstantBlockTintSource::new)
    );

    protected final int color;

    public ConstantBlockTintSource(boolean colorParticle, int color) {
        super(colorParticle);
        this.color = color;
    }

    @Override
    public int color(BlockState state) {
        return color;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public MapCodec<? extends BlockTintSource> getCodec() {
        return CODEC;
    }
}
