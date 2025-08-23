package mod.moineau.contentpacks.block.statefunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

public record ConstantBlockStateFunction<T>(T value) implements BlockStateFunction<T> {
    public static <T> MapCodec<ConstantBlockStateFunction<T>> createCodec(Codec<T> elementCodec, T defaultValue) {
        return elementCodec.xmap(ConstantBlockStateFunction::new, ConstantBlockStateFunction::value).fieldOf("value");
    }

    @Override
    public T apply(@Nullable BlockState state) {
        return value;
    }

    @Override
    public BlockStateFunctionType<?> getType() {
        return BlockStateFunctionType.CONSTANT;
    }
}
