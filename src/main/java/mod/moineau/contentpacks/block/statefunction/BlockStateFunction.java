package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.block.Bakeable;
import mod.moineau.contentpacks.state.PropertiesPredicate;
import mod.moineau.contentpacks.state.StateDefinition;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Function;

public interface BlockStateFunction<T> extends Function<BlockState, T>, Bakeable {
    static <T> Codec<BlockStateFunction<T>> createCodec(Codec<T> elementCodec, T fallback) {
        return BlockStateFunctionType.ID_MAPPER.getCodec(Identifier.CODEC)
                .dispatch(BlockStateFunction::getType, type -> type.codec(elementCodec, fallback));
    }

    @Workaround
    static <T> Codec<Function<BlockState, T>> createDowngradedCodec(Codec<T> elementCodec, T defaultValue) {
        return CodecUtil.downgrade(createCodec(elementCodec, defaultValue), true);
    }

    BlockStateFunctionType<?> getType();

    static <T> BlockStateFunction<T> constant(T value) {
        return new ConstantBlockStateFunction<>(value);
    }

    static <T> BlockStateFunction<T> variants(StateDefinition<T> variants, T fallback) {
        return new VariantsBlockStateFunction.Caching<>(variants, fallback);
    }

    static <T> BlockStateFunction<T> variants(Map<PropertiesPredicate, T> variants, T fallback) {
        return variants(StateDefinition.of(variants), fallback);
    }
}