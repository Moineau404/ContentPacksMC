package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AlwaysEntityTypedStatePredicate implements EntityTypedStatePredicate {
	public static final AlwaysEntityTypedStatePredicate TRUE = new AlwaysEntityTypedStatePredicate() {
		public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
			return true;
		}

		@Override
		public EntityTypedStatePredicateType<?> getType() {
			return EntityTypedStatePredicateType.TRUE;
		}
	};
	public static final AlwaysEntityTypedStatePredicate FALSE = new AlwaysEntityTypedStatePredicate() {
		public boolean test(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type) {
			return false;
		}

		@Override
		public EntityTypedStatePredicateType<?> getType() {
			return EntityTypedStatePredicateType.FALSE;
		}
	};
	public static final MapCodec<AlwaysEntityTypedStatePredicate> TRUE_CODEC = MapCodec.unit(() -> TRUE);
	public static final MapCodec<AlwaysEntityTypedStatePredicate> FALSE_CODEC = MapCodec.unit(() -> FALSE);
}
