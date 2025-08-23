package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public abstract class AlwaysBlockContextPredicate implements BlockContextPredicate {
	public static final AlwaysBlockContextPredicate TRUE = new AlwaysBlockContextPredicate() {
		public boolean test(BlockState state, BlockView world, BlockPos pos) {
			return true;
		}

		@Override
		public BlockContextPredicateType<?> getType() {
			return BlockContextPredicateType.TRUE;
		}
	};
	public static final AlwaysBlockContextPredicate FALSE = new AlwaysBlockContextPredicate() {
		public boolean test(BlockState state, BlockView world, BlockPos pos) {
			return false;
		}

		@Override
		public BlockContextPredicateType<?> getType() {
			return BlockContextPredicateType.FALSE;
		}
	};
	public static final MapCodec<AlwaysBlockContextPredicate> TRUE_CODEC = MapCodec.unit(() -> TRUE);
	public static final MapCodec<AlwaysBlockContextPredicate> FALSE_CODEC = MapCodec.unit(() -> FALSE);
}
