package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.api.math.Comparison;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import java.util.List;
import java.util.Map;

public interface BlockContextPredicate extends AbstractBlock.ContextPredicate {
	Codec<BlockContextPredicate> BASE_CODEC = ContentRegistries.BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatch(BlockContextPredicate::getType, BlockContextPredicateType::codec);
	MapCodec<BlockContextPredicate> MAP_CODEC = ContentRegistries.BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatchMap(BlockContextPredicate::getType, BlockContextPredicateType::codec);
	@Workaround
	Codec<AbstractBlock.ContextPredicate> DOWNGRADED_CODEC = CodecUtil.downgrade(BASE_CODEC, true);

	BlockContextPredicateType<?> getType();

	static BlockContextPredicate allOf(List<BlockContextPredicate> predicates) {
		return new AllOfBlockContextPredicate(predicates);
	}

	static BlockContextPredicate allOf(BlockContextPredicate... predicates) {
		return allOf(List.of(predicates));
	}

	static BlockContextPredicate bothOf(BlockContextPredicate first, BlockContextPredicate second) {
		return allOf(List.of(first, second));
	}

	static BlockContextPredicate anyOf(List<BlockContextPredicate> predicates) {
		return new AnyOfBlockContextPredicate(predicates);
	}

	static BlockContextPredicate anyOf(BlockContextPredicate... predicates) {
		return anyOf(List.of(predicates));
	}

	static BlockContextPredicate eitherOf(BlockContextPredicate first, BlockContextPredicate second) {
		return anyOf(List.of(first, second));
	}

	static BlockContextPredicate matchingBlocks(Vec3i offset, List<Block> blocks) {
		return new MatchingBlocksBlockContextPredicate(offset, RegistryEntryList.of(Block::getRegistryEntry, blocks));
	}

	static BlockContextPredicate matchingBlocks(List<Block> blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static BlockContextPredicate matchingBlocks(Vec3i offset, Block... blocks) {
		return matchingBlocks(offset, List.of(blocks));
	}

	static BlockContextPredicate matchingBlocks(Block... blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static BlockContextPredicate matchingBlockTag(Vec3i offset, TagKey<Block> tag) {
		return new MatchingBlockTagContextPredicate(offset, tag);
	}

	static BlockContextPredicate matchingBlockTag(TagKey<Block> offset) {
		return matchingBlockTag(Vec3i.ZERO, offset);
	}

	static BlockContextPredicate matchingFluids(Vec3i offset, List<Fluid> fluids) {
		return new MatchingFluidsBlockContextPredicate(offset, RegistryEntryList.of(Fluid::getRegistryEntry, fluids));
	}

	static BlockContextPredicate matchingFluids(Vec3i offset, Fluid... fluids) {
		return matchingFluids(offset, List.of(fluids));
	}

	static BlockContextPredicate matchingFluids(Fluid... fluids) {
		return matchingFluids(Vec3i.ZERO, fluids);
	}

	static BlockContextPredicate not(BlockContextPredicate predicate) {
		return new NotBlockContextPredicate(predicate);
	}

	static BlockContextPredicate replaceable(Vec3i offset) {
		return new ReplaceableBlockContextPredicate(offset);
	}

	static BlockContextPredicate replaceable() {
		return replaceable(Vec3i.ZERO);
	}

	static BlockContextPredicate hasSturdyFace(Vec3i offset, Direction face) {
		return new HasSturdyFaceContextPredicate(offset, face);
	}

	static BlockContextPredicate hasSturdyFace(Direction face) {
		return hasSturdyFace(Vec3i.ZERO, face);
	}

	static BlockContextPredicate solid(Vec3i offset) {
		return new SolidBlockContextPredicate(offset);
	}

	static BlockContextPredicate solid() {
		return solid(Vec3i.ZERO);
	}

	static BlockContextPredicate noFluid() {
		return noFluid(Vec3i.ZERO);
	}

	static BlockContextPredicate noFluid(Vec3i offset) {
		return matchingFluids(offset, Fluids.EMPTY);
	}

	static BlockContextPredicate insideWorldBounds(Vec3i offset) {
		return new InsideWorldBoundsBlockContextPredicate(offset);
	}

	static BlockContextPredicate alwaysTrue() {
		return AlwaysBlockContextPredicate.TRUE;
	}

	static BlockContextPredicate alwaysFalse() {
		return AlwaysBlockContextPredicate.FALSE;
	}

	static BlockContextPredicate matchingProperties(Vec3i offset, Map<Property<?>, Comparison<?>> propertyMap) {
		return new MatchingPropertiesBlockContextPredicate(offset, propertyMap);
	}

	static BlockContextPredicate matchingProperties(Map<Property<?>, Comparison<?>> propertyMap) {
		return matchingProperties(Vec3i.ZERO, propertyMap);
	}

	static <T extends Comparable<T>> BlockContextPredicate matchingProperties(Vec3i offset, Property<T> property, Comparison<T> comparison) {
		return matchingProperties(offset, Map.of(property, comparison));
	}

	static <T extends Comparable<T>> BlockContextPredicate matchingProperties(Property<T> property, Comparison<T> comparison) {
		return matchingProperties(Map.of(property, comparison));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> BlockContextPredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> BlockContextPredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> BlockContextPredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> BlockContextPredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static BlockContextPredicate isFullCube() {
		return IsFullCubeBlockContextPredicate.INSTANCE;
	}

	static BlockContextPredicate isSideSolidFullCube(Direction direction) {
		return new IsSideSolidFullCubeBlockContextPredicate(direction);
	}

	static BlockContextPredicate luminance(Comparison<Integer> predicate) {
		return luminance(Vec3i.ZERO, predicate);
	}

	static BlockContextPredicate luminance(Vec3i offset, Comparison<Integer> predicate) {
		return new LuminanceBlockContextPredicate(offset, predicate);
	}

	static BlockContextPredicate blocksMovement() {
		return new BlocksMovementBlockContextPredicate(Vec3i.ZERO);
	}

	static BlockContextPredicate blocksMovement(Vec3i offset) {
		return new BlocksMovementBlockContextPredicate(offset);
	}
}
