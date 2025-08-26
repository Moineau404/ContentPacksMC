package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

abstract class CombinedBlockStatePredicate implements BlockStatePredicate {
	protected final List<BlockStatePredicate> predicates;

	protected CombinedBlockStatePredicate(List<BlockStatePredicate> predicates) {
		this.predicates = predicates;
	}

	public static <T extends CombinedBlockStatePredicate> MapCodec<T> buildCodec(Function<List<BlockStatePredicate>, T> combiner) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(BASE_CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.predicates)).apply(instance, combiner)
		);
	}
}
