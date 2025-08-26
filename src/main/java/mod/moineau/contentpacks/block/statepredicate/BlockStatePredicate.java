package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.function.predicate.Comparator;
import mod.moineau.contentpacks.api.function.predicate.Comparison;
import mod.moineau.contentpacks.block.Bakeable;
import mod.moineau.contentpacks.registry.ContentRegistries;
import mod.moineau.contentpacks.state.PropertiesPredicate;
import mod.moineau.contentpacks.state.PropertyPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.List;
import java.util.function.Predicate;

public interface BlockStatePredicate extends Predicate<BlockState>, Bakeable {
	Codec<BlockStatePredicate> BASE_CODEC = ContentRegistries.BLOCK_STATE_PREDICATE_TYPE.getCodec()
			.dispatch(BlockStatePredicate::getType, BlockStatePredicateType::codec);

	BlockStatePredicateType<?> getType();

	static BlockStatePredicate allOf(List<BlockStatePredicate> predicates) {
		return new AllOfBlockStatePredicate(predicates);
	}

	static BlockStatePredicate allOf(BlockStatePredicate... predicates) {
		return allOf(List.of(predicates));
	}

	static BlockStatePredicate bothOf(BlockStatePredicate first, BlockStatePredicate second) {
		return allOf(List.of(first, second));
	}

	static BlockStatePredicate anyOf(List<BlockStatePredicate> predicates) {
		return new AnyOfBlockStatePredicate(predicates);
	}

	static BlockStatePredicate anyOf(BlockStatePredicate... predicates) {
		return anyOf(List.of(predicates));
	}

	static BlockStatePredicate eitherOf(BlockStatePredicate first, BlockStatePredicate second) {
		return anyOf(List.of(first, second));
	}

	static BlockStatePredicate not(BlockStatePredicate predicate) {
		return new NotBlockStatePredicate(predicate);
	}

	static BlockStatePredicate matchingProperties(PropertiesPredicate predicate) {
		return new MatchingPropertiesBlockStatePredicate(predicate);
	}

	static <T extends Comparable<T>> BlockStatePredicate matchingProperties(Property<T> property, Comparator comparator, T value) {
		return new MatchingPropertiesBlockStatePredicate(PropertiesPredicate.of(PropertyPredicate.of(property, Comparator.EQUAL, value)));
	}

	static <T extends Comparable<T>> BlockStatePredicate matchingProperties(Property<T> property, T value) {
		return matchingProperties(property, Comparator.EQUAL, value);
	}

	static BlockStatePredicate luminance(Comparison<Integer> predicate) {
		return new LuminanceBlockStatePredicate(predicate);
	}

	static BlockStatePredicate blocksMovement() {
		return BlocksMovementBlockStatePredicate.INSTANCE;
	}
}
