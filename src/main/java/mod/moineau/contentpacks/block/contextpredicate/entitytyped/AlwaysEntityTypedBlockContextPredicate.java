package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public abstract class AlwaysEntityTypedBlockContextPredicate implements EntityTypedBlockContextPredicate {
	public static final AlwaysEntityTypedBlockContextPredicate TRUE = new AlwaysEntityTypedBlockContextPredicate() {
		public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
			return true;
		}

		@Override
		public EntityTypedBlockContextPredicateType<?> getType() {
			return EntityTypedBlockContextPredicateType.TRUE;
		}
	};
	public static final AlwaysEntityTypedBlockContextPredicate FALSE = new AlwaysEntityTypedBlockContextPredicate() {
		public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
			return false;
		}

		@Override
		public EntityTypedBlockContextPredicateType<?> getType() {
			return EntityTypedBlockContextPredicateType.FALSE;
		}
	};
	public static final MapCodec<AlwaysEntityTypedBlockContextPredicate> TRUE_CODEC = MapCodec.unit(() -> TRUE);
	public static final MapCodec<AlwaysEntityTypedBlockContextPredicate> FALSE_CODEC = MapCodec.unit(() -> FALSE);
}
