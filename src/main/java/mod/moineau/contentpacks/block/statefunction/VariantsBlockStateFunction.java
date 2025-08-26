package mod.moineau.contentpacks.block.statefunction;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.contentpacks.state.StateDefinition;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import org.jetbrains.annotations.Nullable;

public sealed class VariantsBlockStateFunction<T> implements BlockStateFunction<T> {
    private StateDefinition<T> variants;
    protected final @Nullable T fallback;

    public static <T> MapCodec<VariantsBlockStateFunction<T>> createCodec(Codec<T> elementCodec, T fallback) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                StateDefinition.createCodec(elementCodec).fieldOf("variants").forGetter(function -> function.variants),
                elementCodec.optionalFieldOf("fallback", fallback).forGetter(function -> function.fallback)
        ).apply(instance, VariantsBlockStateFunction::new));
    }

    @Override
    public BlockStateFunctionType<?> getType() {
        return BlockStateFunctionType.VARIANTS;
    }

    public static <T> MapCodec<VariantsBlockStateFunction<T>> createCachingCodec(Codec<T> elementCodec, T fallback) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                StateDefinition.createCodec(elementCodec).fieldOf("variants").forGetter(function -> function.variants),
                elementCodec.optionalFieldOf("fallback", fallback).forGetter(function -> function.fallback)
        ).apply(instance, VariantsBlockStateFunction.Caching::new));
    }

    public VariantsBlockStateFunction(StateDefinition<T> variants, @Nullable T fallback) {
        this.variants = variants;
        this.fallback = fallback;
    }

    @Override
    public T apply(BlockState state) {
        return this.variants.get(state);
    }

    @Override
    public DataResult<?> contentpacks$bake(StateManager<?, ?> stateManager) {
        if (this.variants instanceof StateDefinition.Unbaked<T> unbaked) {
            return unbaked.bake(stateManager).ifSuccess(baked -> this.variants = baked);
        }
        return EMPTY_RESULT;
    }

    public static final class Caching<T> extends VariantsBlockStateFunction<T> {
        private final LoadingCache<BlockState, T> cache = CacheBuilder.newBuilder().build(CacheLoader.from(super::apply));

        public Caching(StateDefinition<T> variants, @Nullable T fallback) {
            super(variants, fallback);
        }

        @Override
        public T apply(BlockState state) {
            try {
                return this.cache.get(state);
            } catch (Exception e) {
                return this.fallback;
            }
        }
    }
}
