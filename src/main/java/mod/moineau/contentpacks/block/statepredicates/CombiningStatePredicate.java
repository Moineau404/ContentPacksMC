package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

abstract class CombiningStatePredicate implements StatePredicate {
	protected final List<StatePredicate> predicates;

	protected CombiningStatePredicate(List<StatePredicate> predicates) {
		this.predicates = predicates;
	}

	public static <T extends CombiningStatePredicate> MapCodec<T> buildCodec(Function<List<StatePredicate>, T> combiner) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(BASE_CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.predicates)).apply(instance, combiner)
		);
	}
}
