package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public record DelegatedEntityTypedBlockContextPredicate(BlockContextPredicate delegate) implements EntityTypedBlockContextPredicate {
    public static final MapCodec<? extends EntityTypedBlockContextPredicate> CODEC = BlockContextPredicate.MAP_CODEC
            .xmap(DelegatedEntityTypedBlockContextPredicate::new, DelegatedEntityTypedBlockContextPredicate::delegate);

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return delegate.test(state, world, pos);
    }

    @Override
    public EntityTypedBlockContextPredicateType<?> getType() {
        return () -> CODEC;
    }
}
