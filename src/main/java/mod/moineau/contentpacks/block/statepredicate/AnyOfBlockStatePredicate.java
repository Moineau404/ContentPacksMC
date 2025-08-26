package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;

import java.util.List;

public class AnyOfBlockStatePredicate extends CombinedBlockStatePredicate {
	public static final MapCodec<AnyOfBlockStatePredicate> CODEC = buildCodec(AnyOfBlockStatePredicate::new);

	public AnyOfBlockStatePredicate(List<BlockStatePredicate> list) {
		super(list);
	}

	public boolean test(BlockState state) {
		for (BlockStatePredicate blockStatePredicate : this.predicates) {
			if (blockStatePredicate.test(state)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BlockStatePredicateType<?> getType() {
		return BlockStatePredicateType.ANY_OF;
	}
}
