package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;

public abstract class AlwaysBlockStatePredicate implements BlockStatePredicate {
	public static final AlwaysBlockStatePredicate TRUE = new AlwaysBlockStatePredicate() {
		public boolean test(BlockState state) {
			return true;
		}

		@Override
		public BlockStatePredicateType<?> getType() {
			return BlockStatePredicateType.TRUE;
		}
	};
	public static final AlwaysBlockStatePredicate FALSE = new AlwaysBlockStatePredicate() {
		public boolean test(BlockState state) {
			return false;
		}

		@Override
		public BlockStatePredicateType<?> getType() {
			return BlockStatePredicateType.FALSE;
		}
	};
	public static final MapCodec<AlwaysBlockStatePredicate> TRUE_CODEC = MapCodec.unit(() -> TRUE);
	public static final MapCodec<AlwaysBlockStatePredicate> FALSE_CODEC = MapCodec.unit(() -> FALSE);
}
