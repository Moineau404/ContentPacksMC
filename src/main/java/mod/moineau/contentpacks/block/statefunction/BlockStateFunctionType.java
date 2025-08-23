package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public interface BlockStateFunctionType<F extends BlockStateFunction<?>> {
    Codecs.IdMapper<Identifier, BlockStateFunctionType<?>> ID_MAPPER = new Codecs.IdMapper<>();
    BlockStateFunctionType<ConstantBlockStateFunction<?>> CONSTANT = ConstantBlockStateFunction::createCodec;
    BlockStateFunctionType<DefinitionBlockStateFunction<?>> DEFINITION = DefinitionBlockStateFunction::createRawCodec;

    <T> MapCodec<? extends BlockStateFunction<T>> codec(Codec<T> elementCodec, T defaultValue);

    static void bootstrap() {
        ID_MAPPER.put(Identifier.ofVanilla("constant"), CONSTANT);
        ID_MAPPER.put(Identifier.ofVanilla("definition"), DEFINITION);
    }
}
