package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AnyOfEntityTypedStatePredicate extends CombiningEntityTypedStatePredicate {
	public static final MapCodec<AnyOfEntityTypedStatePredicate> CODEC = buildCodec(AnyOfEntityTypedStatePredicate::new);

	public AnyOfEntityTypedStatePredicate(List<EntityTypedStatePredicate> list) {
		super(list);
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
		for (EntityTypedStatePredicate entityTypedStatePredicate : this.predicates) {
			if (entityTypedStatePredicate.test(state, world, pos, type)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public EntityTypedStatePredicateType<?> getType() {
		return EntityTypedStatePredicateType.ANY_OF;
	}
}
