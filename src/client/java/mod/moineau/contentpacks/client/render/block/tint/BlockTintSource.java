package mod.moineau.contentpacks.client.render.block.tint;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockTintSource implements net.minecraft.client.color.block.BlockTintSource {
    protected static <T extends BlockTintSource> Products.P1<RecordCodecBuilder.Mu<T>, Boolean> fillContentBlockTintSourceFields(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(Codec.BOOL.optionalFieldOf("particle", true).forGetter(BlockTintSource::doColorParticle));
    }

    protected final boolean colorParticle;

    protected BlockTintSource(boolean colorParticle) {
        this.colorParticle = colorParticle;
    }

    @Override
    public int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return colorParticle ? colorInWorld(state, level, pos) : -1;
    }

    public boolean doColorParticle() {
        return colorParticle;
    }

    abstract MapCodec<? extends BlockTintSource> getCodec();
}
