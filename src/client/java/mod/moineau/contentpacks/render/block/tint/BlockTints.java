package mod.moineau.contentpacks.render.block.tint;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record BlockTints(List<BlockTintSource> tints) implements BlockColorProvider {
    public static final Codec<BlockTints> CODEC = BlockTintSourceTypes.CODEC.listOf().xmap(BlockTints::new, BlockTints::tints);

    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        BlockTintSource tint = this.tints.get(tintIndex);
        return tint != null ? tint.getColor(state, world, pos, tintIndex) : -1;
    }
}