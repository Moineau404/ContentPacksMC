package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public class AnyOfEntityTypedBlockContextPredicate extends CombinedEntityTypedBlockContextPredicate {
	public static final MapCodec<AnyOfEntityTypedBlockContextPredicate> CODEC = buildCodec(AnyOfEntityTypedBlockContextPredicate::new);

	public AnyOfEntityTypedBlockContextPredicate(List<EntityTypedBlockContextPredicate> list) {
		super(list);
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		for (EntityTypedBlockContextPredicate entityTypedBlockContextPredicate : this.predicates) {
			if (entityTypedBlockContextPredicate.test(state, world, pos, type)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public EntityTypedBlockContextPredicateType<?> getType() {
		return EntityTypedBlockContextPredicateType.ANY_OF;
	}
}
