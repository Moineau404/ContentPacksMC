package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.block.BlockStateDefinition;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

public final class DefinitionBlockStateFunction<T> extends CachingBlockStateFunction<T> {
    public static <T> MapCodec<DefinitionBlockStateFunction<T>> createCodec(Codec<T> elementCodec, T defaultValue) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockStateDefinition.createCodec(elementCodec).fieldOf("variants").forGetter(function -> function.definition),
                elementCodec.optionalFieldOf("default", defaultValue).forGetter(function -> function.defaultValue)
        ).apply(instance, DefinitionBlockStateFunction::new));
    }

    public static <T> MapCodec<DefinitionBlockStateFunction<T>> createRawCodec(Codec<T> elementCodec, T defaultValue) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockStateDefinition.createRawCodec(elementCodec).fieldOf("variants").forGetter(function -> function.definition),
                elementCodec.optionalFieldOf("default", defaultValue).forGetter(function -> function.defaultValue)
        ).apply(instance, DefinitionBlockStateFunction::new));
    }

    private final BlockStateDefinition<T> definition;

    public DefinitionBlockStateFunction(BlockStateDefinition<T> definition, T defaultValue) {
        super(defaultValue);
        this.definition = definition;
    }

    @Override
    protected @Nullable T load(BlockState state) {
        return definition.get(state);
    }

    @Override
    public BlockStateFunctionType<?> getType() {
        return BlockStateFunctionType.DEFINITION;
    }
}
