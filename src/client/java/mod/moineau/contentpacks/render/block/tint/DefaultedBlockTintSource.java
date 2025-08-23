package mod.moineau.contentpacks.render.block.tint;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class DefaultedBlockTintSource implements BlockTintSource {
    protected static <T extends DefaultedBlockTintSource> Products.P1<RecordCodecBuilder.Mu<T>, BlockTintSource> fillDefaultedBlockTintSourceFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(BlockTintSourceTypes.CODEC.optionalFieldOf("default", BlockTintSource.NO_TINT).forGetter(DefaultedBlockTintSource::getDefaultTintSource));
    }

    private final BlockTintSource defaultTintSource;

    public DefaultedBlockTintSource(BlockTintSource defaultTintSource) {
        this.defaultTintSource = defaultTintSource;
    }

    @Override
    public final int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return world != null && pos != null ? getValidatedColor(state, world, pos, tintIndex) : defaultTintSource.getColor(state, world, pos, tintIndex);
    }

    public abstract int getValidatedColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex);

    public BlockTintSource getDefaultTintSource() {
        return defaultTintSource;
    }
}
