package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;

public class NotBlockStatePredicate implements BlockStatePredicate {
	public static final MapCodec<NotBlockStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate))
			.apply(instance, NotBlockStatePredicate::new)
	);
	private final BlockStatePredicate predicate;

	public NotBlockStatePredicate(BlockStatePredicate predicate) {
		this.predicate = predicate;
	}

	public boolean test(BlockState state) {
		return !this.predicate.test(state);
	}

	@Override
	public BlockStatePredicateType<?> getType() {
		return BlockStatePredicateType.NOT;
	}
}
