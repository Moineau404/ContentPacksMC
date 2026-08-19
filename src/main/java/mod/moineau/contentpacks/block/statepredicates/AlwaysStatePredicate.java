package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AlwaysStatePredicate implements StatePredicate, StatePredicateType<AlwaysStatePredicate> {
	public static final AlwaysStatePredicate TRUE = new AlwaysStatePredicate() {
		@Override
		public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
			return true;
		}
	};
	public static final AlwaysStatePredicate FALSE = new AlwaysStatePredicate() {
		@Override
		public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
			return false;
		}
	};

	private final MapCodec<AlwaysStatePredicate> codec;

	private AlwaysStatePredicate() {
        this.codec = MapCodec.unit(this);
    }

	@Override
	public final MapCodec<AlwaysStatePredicate> codec() {
		return this.codec;
	}

	@Override
	public StatePredicateType<?> getType() {
		return this;
	}
}
