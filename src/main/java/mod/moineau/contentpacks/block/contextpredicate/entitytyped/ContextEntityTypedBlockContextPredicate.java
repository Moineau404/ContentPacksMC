package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public record ContextEntityTypedBlockContextPredicate(BlockContextPredicate predicate) implements EntityTypedBlockContextPredicate {
    public static final MapCodec<? extends EntityTypedBlockContextPredicate> CODEC = BlockContextPredicate.MAP_CODEC
            .xmap(ContextEntityTypedBlockContextPredicate::new, ContextEntityTypedBlockContextPredicate::predicate);

    @Override
    public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return predicate.test(state, world, pos);
    }

    @Override
    public EntityTypedBlockContextPredicateType<?> getType() {
        return () -> CODEC;
    }
}
