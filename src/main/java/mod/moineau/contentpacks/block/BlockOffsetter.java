package mod.moineau.contentpacks.block;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

// TODO Re-implement offsetter
@Deprecated
public abstract class BlockOffsetter implements AbstractBlock.Offsetter {
    public static final BlockOffsetter XZ = new BlockOffsetter() {
        @Override
        public AbstractBlock.OffsetType getType() {
            return AbstractBlock.OffsetType.XZ;
        }

        @Override
        public Vec3d evaluate(BlockState state, BlockPos pos) {
            Block block = state.getBlock();
            long l = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
            float f = block.getMaxHorizontalModelOffset();
            double d = MathHelper.clamp(((float)(l & 15L) / 15.0F - 0.5) * 0.5, -f, f);
            double e = MathHelper.clamp(((float)(l >> 8 & 15L) / 15.0F - 0.5) * 0.5, -f, f);
            return new Vec3d(d, 0.0, e);
        }
    };
    public static final BlockOffsetter XYZ = new BlockOffsetter() {
        @Override
        public AbstractBlock.OffsetType getType() {
            return AbstractBlock.OffsetType.XYZ;
        }

        @Override
        public Vec3d evaluate(BlockState state, BlockPos pos) {
            Block block = state.getBlock();
            long l = MathHelper.hashCode(pos.getX(), 0, pos.getZ());
            double d = ((float)(l >> 4 & 15L) / 15.0F - 1.0) * block.getVerticalModelOffsetMultiplier();
            float f = block.getMaxHorizontalModelOffset();
            double e = MathHelper.clamp(((float)(l & 15L) / 15.0F - 0.5) * 0.5, -f, f);
            double g = MathHelper.clamp(((float)(l >> 8 & 15L) / 15.0F - 0.5) * 0.5, -f, f);
            return new Vec3d(e, d, g);
        }
    };
    public static final Codec<BlockOffsetter> CODEC = CodecUtil.enumByName(AbstractBlock.OffsetType.class)
            .xmap(BlockOffsetter::getOffsetter, BlockOffsetter::getOffsetType);

    public static @Nullable BlockOffsetter getOffsetter(AbstractBlock.OffsetType offsetType) {
        return switch (offsetType) {
            case NONE -> null;
            case XZ -> XZ;
            case XYZ -> XYZ;
        };
    }

    public static AbstractBlock.OffsetType getOffsetType(@Nullable BlockOffsetter offsetter) {
        return offsetter != null ? offsetter.getType() : AbstractBlock.OffsetType.NONE;
    }

    protected abstract AbstractBlock.OffsetType getType();

    @Override
    public final boolean equals(Object obj) {
        return this == obj || (obj instanceof BlockOffsetter offsetter && this.getType() == offsetter.getType());
    }
}