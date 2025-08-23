package mod.moineau.contentpacks.block.statefunction;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public abstract class CachingBlockStateFunction<T> implements BlockStateFunction<T> {
    protected final T defaultValue;
    private final LoadingCache<BlockState, T> cache = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull T load(@NotNull BlockState state) {
            return Objects.requireNonNullElse(CachingBlockStateFunction.this.load(state), CachingBlockStateFunction.this.defaultValue);
        }
    });

    protected CachingBlockStateFunction(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public final T apply(BlockState state) {
        try {
            return cache.get(state);
        } catch (ExecutionException e) {
            return defaultValue;
        }
    }

    protected abstract @Nullable T load(BlockState state);
}
