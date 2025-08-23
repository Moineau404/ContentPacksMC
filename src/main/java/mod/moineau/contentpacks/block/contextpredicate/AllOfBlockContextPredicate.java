package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public class AllOfBlockContextPredicate extends CombinedBlockContextPredicate {
	public static final MapCodec<AllOfBlockContextPredicate> CODEC = buildCodec(AllOfBlockContextPredicate::new);

	public AllOfBlockContextPredicate(List<BlockContextPredicate> list) {
		super(list);
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos) {
		for (BlockContextPredicate blockContextPredicate : this.predicates) {
			if (!blockContextPredicate.test(state, world, pos)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.ALL_OF;
	}
}
