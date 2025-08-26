package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;

import java.util.List;

public class AllOfBlockStatePredicate extends CombinedBlockStatePredicate {
	public static final MapCodec<AllOfBlockStatePredicate> CODEC = buildCodec(AllOfBlockStatePredicate::new);

	public AllOfBlockStatePredicate(List<BlockStatePredicate> list) {
		super(list);
	}

	public boolean test(BlockState state) {
		for (BlockStatePredicate blockStatePredicate : this.predicates) {
			if (!blockStatePredicate.test(state)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public BlockStatePredicateType<?> getType() {
		return BlockStatePredicateType.ALL_OF;
	}
}
