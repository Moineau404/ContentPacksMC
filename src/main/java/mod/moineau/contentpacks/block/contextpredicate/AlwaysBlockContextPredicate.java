package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public abstract class AlwaysBlockContextPredicate implements BlockContextPredicate, BlockContextPredicateType<AlwaysBlockContextPredicate> {
	public static final AlwaysBlockContextPredicate TRUE = new AlwaysBlockContextPredicate() {
		@Override
		public boolean test(BlockState state, BlockView world, BlockPos pos) {
			return true;
		}
	};
	public static final AlwaysBlockContextPredicate FALSE = new AlwaysBlockContextPredicate() {
		@Override
		public boolean test(BlockState state, BlockView world, BlockPos pos) {
			return false;
		}
	};

	private final MapCodec<AlwaysBlockContextPredicate> codec;

	private AlwaysBlockContextPredicate() {
        this.codec = MapCodec.unit(this);
    }

	@Override
	public final MapCodec<AlwaysBlockContextPredicate> codec() {
		return this.codec;
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return this;
	}
}
