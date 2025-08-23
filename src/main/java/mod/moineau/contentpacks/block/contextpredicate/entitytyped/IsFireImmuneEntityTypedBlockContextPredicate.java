package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class IsFireImmuneEntityTypedBlockContextPredicate implements EntityTypedBlockContextPredicate {
    public static final IsFireImmuneEntityTypedBlockContextPredicate INSTANCE = new IsFireImmuneEntityTypedBlockContextPredicate();
    public static final MapCodec<IsFireImmuneEntityTypedBlockContextPredicate> CODEC = MapCodec.unit(INSTANCE);

    private IsFireImmuneEntityTypedBlockContextPredicate() {}

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type.isFireImmune();
    }

    @Override
    public EntityTypedBlockContextPredicateType<?> getType() {
        return EntityTypedBlockContextPredicateType.IS_FIRE_IMMUNE;
    }
}
