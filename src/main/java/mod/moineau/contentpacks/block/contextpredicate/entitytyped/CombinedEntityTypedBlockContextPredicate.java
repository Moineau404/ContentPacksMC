package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

abstract class CombinedEntityTypedBlockContextPredicate implements EntityTypedBlockContextPredicate {
	protected final List<EntityTypedBlockContextPredicate> predicates;

	protected CombinedEntityTypedBlockContextPredicate(List<EntityTypedBlockContextPredicate> predicates) {
		this.predicates = predicates;
	}

	public static <T extends CombinedEntityTypedBlockContextPredicate> MapCodec<T> buildCodec(Function<List<EntityTypedBlockContextPredicate>, T> combiner) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(BASE_CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.predicates)).apply(instance, combiner)
		);
	}
}
