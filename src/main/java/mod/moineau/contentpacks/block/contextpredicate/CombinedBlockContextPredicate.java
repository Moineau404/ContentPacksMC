package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.function.Function;

abstract class CombinedBlockContextPredicate implements BlockContextPredicate {
	protected final List<BlockContextPredicate> predicates;

	protected CombinedBlockContextPredicate(List<BlockContextPredicate> predicates) {
		this.predicates = predicates;
	}

	public static <T extends CombinedBlockContextPredicate> MapCodec<T> buildCodec(Function<List<BlockContextPredicate>, T> combiner) {
		return RecordCodecBuilder.mapCodec(
			instance -> instance.group(BASE_CODEC.listOf().fieldOf("predicates").forGetter(predicate -> predicate.predicates)).apply(instance, combiner)
		);
	}
}
