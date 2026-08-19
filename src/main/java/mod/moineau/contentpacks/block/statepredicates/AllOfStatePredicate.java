package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AllOfStatePredicate extends CombiningStatePredicate {
	public static final MapCodec<AllOfStatePredicate> CODEC = buildCodec(AllOfStatePredicate::new);

	public AllOfStatePredicate(List<StatePredicate> list) {
		super(list);
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
		for (StatePredicate statePredicate : this.predicates) {
			if (!statePredicate.test(state, world, pos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.ALL_OF;
	}
}
