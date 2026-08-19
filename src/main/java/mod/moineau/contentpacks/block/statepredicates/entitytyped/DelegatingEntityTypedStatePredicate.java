package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public record DelegatingEntityTypedStatePredicate(StatePredicate delegate) implements EntityTypedStatePredicate {
    public static final MapCodec<? extends EntityTypedStatePredicate> CODEC = StatePredicate.MAP_CODEC
            .xmap(DelegatingEntityTypedStatePredicate::new, DelegatingEntityTypedStatePredicate::delegate);

    @Override
    public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return delegate.test(state, world, pos);
    }

    @Override
    public EntityTypedStatePredicateType<?> getType() {
        return () -> CODEC;
    }
}
