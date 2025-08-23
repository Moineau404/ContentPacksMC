package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.List;

public class AnyOfBlockContextPredicate extends CombinedBlockContextPredicate {
	public static final MapCodec<AnyOfBlockContextPredicate> CODEC = buildCodec(AnyOfBlockContextPredicate::new);

	public AnyOfBlockContextPredicate(List<BlockContextPredicate> list) {
		super(list);
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos) {
		for (BlockContextPredicate blockContextPredicate : this.predicates) {
			if (blockContextPredicate.test(state, world, pos)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.ANY_OF;
	}
}
