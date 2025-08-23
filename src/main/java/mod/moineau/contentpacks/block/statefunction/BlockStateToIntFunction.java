package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import net.minecraft.block.BlockState;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.ToIntFunction;

public record BlockStateToIntFunction(BlockStateFunction<Integer> delegate) implements ToIntFunction<BlockState> {
    public static final BlockStateToIntFunction ZERO = new BlockStateToIntFunction(new ConstantBlockStateFunction<>(0));
    public static final Codec<BlockStateToIntFunction> CODEC = BlockStateFunction.createCodec(Codec.INT, 0)
            .xmap(BlockStateToIntFunction::new, BlockStateToIntFunction::delegate);
    @Workaround
    public static final Codec<ToIntFunction<BlockState>> DOWNGRADED_CODEC = CodecUtil.downgrade(CODEC, true);

    public static Codec<BlockStateToIntFunction> createCodec(int min, int max) {
        return BlockStateFunction.createCodec(Codecs.rangedInt(min, max), 0)
                .xmap(BlockStateToIntFunction::new, BlockStateToIntFunction::delegate);
    }

    @Workaround
    public static Codec<ToIntFunction<BlockState>> createDowngradedCodec(int min, int max) {
        return CodecUtil.downgrade(createCodec(min, max), true);
    }

    @Override
    public int applyAsInt(BlockState state) {
        return delegate.apply(state);
    }
}
