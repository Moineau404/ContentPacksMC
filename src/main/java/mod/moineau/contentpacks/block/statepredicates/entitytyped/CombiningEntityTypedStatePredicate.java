package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

abstract class CombiningEntityTypedStatePredicate implements EntityTypedStatePredicate {
	protected final List<EntityTypedStatePredicate> predicates;

	protected CombiningEntityTypedStatePredicate(List<EntityTypedStatePredicate> predicates) {
		this.predicates = predicates;
	}

	public static <T extends CombiningEntityTypedStatePredicate> MapCodec<T> buildCodec(Function<List<EntityTypedStatePredicate>, T> combiner) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(BASE_CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.predicates)).apply(instance, combiner)
		);
	}
}
