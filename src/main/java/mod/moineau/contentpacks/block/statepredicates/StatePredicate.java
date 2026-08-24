package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.moineau.api.math.Comparison;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.api.util.Workaround;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import java.util.List;
import java.util.Map;

public interface StatePredicate extends BlockBehaviour.StatePredicate {
	Codec<StatePredicate> BASE_CODEC = ContentRegistries.STATE_PREDICATE_TYPE.byNameCodec()
			.dispatch(StatePredicate::getType, StatePredicateType::codec);
	MapCodec<StatePredicate> MAP_CODEC = ContentRegistries.STATE_PREDICATE_TYPE.byNameCodec()
			.dispatchMap(StatePredicate::getType, StatePredicateType::codec);
	@Workaround
	Codec<BlockBehaviour.StatePredicate> DOWNGRADED_CODEC = CodecUtil.downgrade(BASE_CODEC, true);

	StatePredicateType<?> getType();

	static StatePredicate allOf(List<StatePredicate> predicates) {
		return new AllOfStatePredicate(predicates);
	}

	static StatePredicate allOf(StatePredicate... predicates) {
		return allOf(List.of(predicates));
	}

	static StatePredicate bothOf(StatePredicate first, StatePredicate second) {
		return allOf(List.of(first, second));
	}

	static StatePredicate anyOf(List<StatePredicate> predicates) {
		return new AnyOfStatePredicate(predicates);
	}

	static StatePredicate anyOf(StatePredicate... predicates) {
		return anyOf(List.of(predicates));
	}

	static StatePredicate eitherOf(StatePredicate first, StatePredicate second) {
		return anyOf(List.of(first, second));
	}

	static StatePredicate matchingBlocks(Vec3i offset, List<Block> blocks) {
		return new MatchingBlocksStatePredicate(offset, HolderSet.direct(Block::builtInRegistryHolder, blocks));
	}

	static StatePredicate matchingBlocks(List<Block> blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static StatePredicate matchingBlocks(Vec3i offset, Block... blocks) {
		return matchingBlocks(offset, List.of(blocks));
	}

	static StatePredicate matchingBlocks(Block... blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static StatePredicate matchingBlockTag(Vec3i offset, TagKey<Block> tag) {
		return new MatchingStatePredicate(offset, tag);
	}

	static StatePredicate matchingBlockTag(TagKey<Block> offset) {
		return matchingBlockTag(Vec3i.ZERO, offset);
	}

	static StatePredicate matchingFluids(Vec3i offset, List<Fluid> fluids) {
		return new MatchingFluidsStatePredicate(offset, HolderSet.direct(Fluid::builtInRegistryHolder, fluids));
	}

	static StatePredicate matchingFluids(Vec3i offset, Fluid... fluids) {
		return matchingFluids(offset, List.of(fluids));
	}

	static StatePredicate matchingFluids(Fluid... fluids) {
		return matchingFluids(Vec3i.ZERO, fluids);
	}

	static StatePredicate not(StatePredicate predicate) {
		return new NotStatePredicate(predicate);
	}

	static StatePredicate replaceable(Vec3i offset) {
		return new ReplaceableStatePredicate(offset);
	}

	static StatePredicate replaceable() {
		return replaceable(Vec3i.ZERO);
	}

	static StatePredicate hasSturdyFace(Vec3i offset, Direction face) {
		return new HasSturdyFaceContextPredicate(offset, face);
	}

	static StatePredicate hasSturdyFace(Direction face) {
		return hasSturdyFace(Vec3i.ZERO, face);
	}

	static StatePredicate solid(Vec3i offset) {
		return new SolidStatePredicate(offset);
	}

	static StatePredicate solid() {
		return solid(Vec3i.ZERO);
	}

	static StatePredicate noFluid() {
		return noFluid(Vec3i.ZERO);
	}

	static StatePredicate noFluid(Vec3i offset) {
		return matchingFluids(offset, Fluids.EMPTY);
	}

	static StatePredicate insideWorldBounds(Vec3i offset) {
		return new InsideWorldBoundsStatePredicate(offset);
	}

	static StatePredicate alwaysTrue() {
		return AlwaysStatePredicate.TRUE;
	}

	static StatePredicate alwaysFalse() {
		return AlwaysStatePredicate.FALSE;
	}

	static StatePredicate matchingProperties(Vec3i offset, Map<Property<?>, Comparison<?>> propertyMap) {
		return new MatchingPropertiesStatePredicate(offset, propertyMap);
	}

	static StatePredicate matchingProperties(Map<Property<?>, Comparison<?>> propertyMap) {
		return matchingProperties(Vec3i.ZERO, propertyMap);
	}

	static <T extends Comparable<T>> StatePredicate matchingProperties(Vec3i offset, Property<T> property, Comparison<T> comparison) {
		return matchingProperties(offset, Map.of(property, comparison));
	}

	static <T extends Comparable<T>> StatePredicate matchingProperties(Property<T> property, Comparison<T> comparison) {
		return matchingProperties(Map.of(property, comparison));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> StatePredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> StatePredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> StatePredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> StatePredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static StatePredicate isFullCube() {
		return IsCollisionShapeFullBlockStatePredicate.INSTANCE;
	}

	static StatePredicate isSideSolidFullCube(Direction direction) {
		return new IsFaceSturdyStatePredicate(direction);
	}

	static StatePredicate lightEmission(Comparison<Integer> predicate) {
		return lightEmission(Vec3i.ZERO, predicate);
	}

	static StatePredicate lightEmission(Vec3i offset, Comparison<Integer> predicate) {
		return new LightEmissionStatePredicate(offset, predicate);
	}

	static StatePredicate blocksMovement() {
		return new BlocksMotionStatePredicate(Vec3i.ZERO);
	}

	static StatePredicate blocksMovement(Vec3i offset) {
		return new BlocksMotionStatePredicate(offset);
	}
}
