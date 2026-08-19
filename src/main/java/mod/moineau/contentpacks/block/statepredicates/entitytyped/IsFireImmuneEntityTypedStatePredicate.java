package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public final class IsFireImmuneEntityTypedStatePredicate implements EntityTypedStatePredicate {
    public static final IsFireImmuneEntityTypedStatePredicate INSTANCE = new IsFireImmuneEntityTypedStatePredicate();
    public static final MapCodec<IsFireImmuneEntityTypedStatePredicate> CODEC = MapCodec.unit(INSTANCE);

    private IsFireImmuneEntityTypedStatePredicate() {}

    @Override
    public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
        return type.fireImmune();
    }

    @Override
    public EntityTypedStatePredicateType<?> getType() {
        return EntityTypedStatePredicateType.IS_FIRE_IMMUNE;
    }
}
