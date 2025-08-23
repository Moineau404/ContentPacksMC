package mod.moineau.contentpacks.block;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class CachingBlockPropertiesPredicate extends BlockPropertiesPredicate {
    public static final Codec<CachingBlockPropertiesPredicate> CODEC = Codec.STRING
            .xmap(CachingBlockPropertiesPredicate::new, CachingBlockPropertiesPredicate::toString);

    private final LoadingCache<BlockState, Boolean> cache = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public @NotNull Boolean load(@NotNull BlockState state) {
            return CachingBlockPropertiesPredicate.this.load(state);
        }
    });

    private CachingBlockPropertiesPredicate(String predicate) {
        super(predicate);
    }

    @Override
    public boolean test(@NotNull BlockState state) {
        try {
            return cache.get(state);
        } catch (ExecutionException e) {
            return false;
        }
    }

    public boolean load(@NotNull BlockState state) {
        return super.test(state);
    }
}

