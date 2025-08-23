package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public interface BlockStateFunction<T> extends Function<BlockState, T> {
    static <T> Codec<BlockStateFunction<T>> createCodec(Codec<T> elementCodec, T defaultValue) {
        return BlockStateFunctionType.ID_MAPPER.getCodec(Identifier.CODEC)
                .dispatch(BlockStateFunction::getType, type -> type.codec(elementCodec, defaultValue));
    }

    @Workaround
    static <T> Codec<Function<BlockState, T>> createDowngradedCodec(Codec<T> elementCodec, T defaultValue) {
        return CodecUtil.downgrade(createCodec(elementCodec, defaultValue), true);
    }

    BlockStateFunctionType<?> getType();
}
