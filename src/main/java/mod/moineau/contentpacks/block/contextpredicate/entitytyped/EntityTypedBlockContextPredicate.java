package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.math.Comparison;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import mod.moineau.contentpacks.event.ContentPacksEvents;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface EntityTypedBlockContextPredicate extends AbstractBlock.TypedContextPredicate<EntityType<?>> {
	Codec<EntityTypedBlockContextPredicate> BASE_CODEC = ContentRegistries.ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatch(EntityTypedBlockContextPredicate::getType, EntityTypedBlockContextPredicateType::codec);
	Codec<EntityTypedBlockContextPredicate> EXTENDED_CODEC = Codec.withAlternative(BASE_CODEC,
			DelegatedEntityTypedBlockContextPredicate.CODEC.codec());
	@Workaround
	Codec<AbstractBlock.TypedContextPredicate<EntityType<?>>> DOWNGRADED_CODEC = CodecUtil.downgrade(EXTENDED_CODEC, true);

	EntityTypedBlockContextPredicateType<?> getType();

	private static EntityTypedBlockContextPredicate delegate(BlockContextPredicate delegate) {
		return new DelegatedEntityTypedBlockContextPredicate(delegate);
	}

	static EntityTypedBlockContextPredicate allOf(List<EntityTypedBlockContextPredicate> predicates) {
		return new AllOfEntityTypedBlockContextPredicate(predicates);
	}

	static EntityTypedBlockContextPredicate allOf(EntityTypedBlockContextPredicate... predicates) {
		return allOf(List.of(predicates));
	}

	static EntityTypedBlockContextPredicate bothOf(EntityTypedBlockContextPredicate first, EntityTypedBlockContextPredicate second) {
		return allOf(List.of(first, second));
	}

	static EntityTypedBlockContextPredicate anyOf(List<EntityTypedBlockContextPredicate> predicates) {
		return new AnyOfEntityTypedBlockContextPredicate(predicates);
	}

	static EntityTypedBlockContextPredicate anyOf(EntityTypedBlockContextPredicate... predicates) {
		return anyOf(List.of(predicates));
	}

	static EntityTypedBlockContextPredicate eitherOf(EntityTypedBlockContextPredicate first, EntityTypedBlockContextPredicate second) {
		return anyOf(List.of(first, second));
	}

	static EntityTypedBlockContextPredicate matchingBlocks(Vec3i offset, List<Block> blocks) {
		return delegate(BlockContextPredicate.matchingBlocks(offset, blocks));
	}

	static EntityTypedBlockContextPredicate matchingBlocks(List<Block> blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static EntityTypedBlockContextPredicate matchingBlocks(Vec3i offset, Block... blocks) {
		return matchingBlocks(offset, List.of(blocks));
	}

	static EntityTypedBlockContextPredicate matchingBlocks(Block... blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static EntityTypedBlockContextPredicate matchingBlockTag(Vec3i offset, TagKey<Block> tag) {
		return delegate(BlockContextPredicate.matchingBlockTag(offset, tag));
	}

	static EntityTypedBlockContextPredicate matchingBlockTag(TagKey<Block> offset) {
		return matchingBlockTag(Vec3i.ZERO, offset);
	}

	static EntityTypedBlockContextPredicate matchingFluids(Vec3i offset, List<Fluid> fluids) {
		return delegate(BlockContextPredicate.matchingFluids(offset, fluids));
	}

	static EntityTypedBlockContextPredicate matchingFluids(Vec3i offset, Fluid... fluids) {
		return matchingFluids(offset, List.of(fluids));
	}

	static EntityTypedBlockContextPredicate matchingFluids(Fluid... fluids) {
		return matchingFluids(Vec3i.ZERO, fluids);
	}

	static EntityTypedBlockContextPredicate not(EntityTypedBlockContextPredicate predicate) {
		return new NotEntityTypedBlockContextPredicate(predicate);
	}

	static EntityTypedBlockContextPredicate replaceable(Vec3i offset) {
		return delegate(BlockContextPredicate.replaceable(offset));
	}

	static EntityTypedBlockContextPredicate replaceable() {
		return replaceable(Vec3i.ZERO);
	}

	static EntityTypedBlockContextPredicate hasSturdyFace(Vec3i offset, Direction face) {
		return delegate(BlockContextPredicate.hasSturdyFace(offset, face));
	}

	static EntityTypedBlockContextPredicate hasSturdyFace(Direction face) {
		return hasSturdyFace(Vec3i.ZERO, face);
	}

	static EntityTypedBlockContextPredicate solid(Vec3i offset) {
		return delegate(BlockContextPredicate.solid(offset));
	}

	static EntityTypedBlockContextPredicate solid() {
		return solid(Vec3i.ZERO);
	}

	static EntityTypedBlockContextPredicate noFluid() {
		return noFluid(Vec3i.ZERO);
	}

	static EntityTypedBlockContextPredicate noFluid(Vec3i offset) {
		return matchingFluids(offset, Fluids.EMPTY);
	}

	static EntityTypedBlockContextPredicate insideWorldBounds(Vec3i offset) {
		return delegate(BlockContextPredicate.insideWorldBounds(offset));
	}

	static EntityTypedBlockContextPredicate alwaysTrue() {
		return AlwaysEntityTypedBlockContextPredicate.TRUE;
	}

	static EntityTypedBlockContextPredicate alwaysFalse() {
		return AlwaysEntityTypedBlockContextPredicate.TRUE;
	}

	static EntityTypedBlockContextPredicate matchingProperties(Vec3i offset, Map<Property<?>, Comparison<?>> propertyMap) {
		return delegate(BlockContextPredicate.matchingProperties(offset, propertyMap));
	}

	static EntityTypedBlockContextPredicate matchingProperties(Map<Property<?>, Comparison<?>> propertyMap) {
		return matchingProperties(Vec3i.ZERO, propertyMap);
	}

	static <T extends Comparable<T>> EntityTypedBlockContextPredicate matchingProperties(Vec3i offset, Property<T> property, Comparison<T> comparison) {
		return matchingProperties(offset, Map.of(property, comparison));
	}

	static <T extends Comparable<T>> EntityTypedBlockContextPredicate matchingProperties(Property<T> property, Comparison<T> comparison) {
		return matchingProperties(Map.of(property, comparison));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> EntityTypedBlockContextPredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> EntityTypedBlockContextPredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> EntityTypedBlockContextPredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> EntityTypedBlockContextPredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static EntityTypedBlockContextPredicate isFullCube() {
		return delegate(BlockContextPredicate.isFullCube());
	}

	static EntityTypedBlockContextPredicate isSideSolidFullCube(Direction direction) {
		return delegate(BlockContextPredicate.isSideSolidFullCube(direction));
	}

	static EntityTypedBlockContextPredicate luminance(Comparison<Integer> predicate) {
		return luminance(Vec3i.ZERO, predicate);
	}

	static EntityTypedBlockContextPredicate luminance(Vec3i offset, Comparison<Integer> predicate) {
		return delegate(BlockContextPredicate.luminance(offset, predicate));
	}

	static EntityTypedBlockContextPredicate blocksMovement() {
		return blocksMovement(Vec3i.ZERO);
	}

	static EntityTypedBlockContextPredicate blocksMovement(Vec3i offset) {
		return delegate(BlockContextPredicate.blocksMovement(offset));
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedBlockContextPredicate whitelist(List<EntityType<?>> types) {
		return new WhitelistEntityTypedBlockContextPredicate(types);
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedBlockContextPredicate whitelist(EntityType<?>... types) {
		return whitelist(List.of(types));
	}

	@Workaround
	@ApiStatus.Internal
	static EntityTypedBlockContextPredicate whitelist(Supplier<List<EntityType<?>>> types) {
		WhitelistEntityTypedBlockContextPredicate predicate = new WhitelistEntityTypedBlockContextPredicate(new ArrayList<>());
		ContentPacksEvents.REGISTRIES_LOADED.register(() -> predicate.list().addAll(types.get()));
		return predicate;
	}

	static EntityTypedBlockContextPredicate isFireImmune() {
		return IsFireImmuneEntityTypedBlockContextPredicate.INSTANCE;
	}
}
