package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ConstantBlockTintSource(int value) implements BlockTintSource {
    public static final MapCodec<ConstantBlockTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.RGB.fieldOf("value").forGetter(ConstantBlockTintSource::value)
            ).apply(instance, ConstantBlockTintSource::new)
    );

    public ConstantBlockTintSource {
        value = ColorHelper.fullAlpha(value);
    }

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return this.value;
    }

    @Override
    public MapCodec<ConstantBlockTintSource> getCodec() {
        return CODEC;
    }
}