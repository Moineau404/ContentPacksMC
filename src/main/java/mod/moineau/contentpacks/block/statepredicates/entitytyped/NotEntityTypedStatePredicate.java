package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class NotEntityTypedStatePredicate implements EntityTypedStatePredicate {
	public static final MapCodec<NotEntityTypedStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate))
			.apply(instance, NotEntityTypedStatePredicate::new)
	);
	private final EntityTypedStatePredicate predicate;

	public NotEntityTypedStatePredicate(EntityTypedStatePredicate predicate) {
		this.predicate = predicate;
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
		return !this.predicate.test(state, world, pos, type);
	}

	@Override
	public EntityTypedStatePredicateType<?> getType() {
		return EntityTypedStatePredicateType.NOT;
	}
}
