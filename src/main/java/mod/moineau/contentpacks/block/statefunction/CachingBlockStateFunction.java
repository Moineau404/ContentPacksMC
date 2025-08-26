package mod.moineau.contentpacks.block.statefunction;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Deprecated(forRemoval = true)
public abstract class CachingBlockStateFunction<T> implements BlockStateFunction<T> {
    protected final T fallback;
    private final LoadingCache<BlockState, T> cache = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull T load(@NotNull BlockState state) {
            return Objects.requireNonNullElse(CachingBlockStateFunction.this.load(state), CachingBlockStateFunction.this.fallback);
        }
    });

    protected CachingBlockStateFunction(T fallback) {
        this.fallback = fallback;
    }

    @Override
    public final T apply(BlockState state) {
        try {
            return cache.get(state);
        } catch (ExecutionException e) {
            return this.fallback;
        }
    }

    protected abstract @Nullable T load(BlockState state);
}
