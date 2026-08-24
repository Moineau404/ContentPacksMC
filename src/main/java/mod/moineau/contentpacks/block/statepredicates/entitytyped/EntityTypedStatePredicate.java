package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.Codec;
import mod.moineau.api.math.Comparison;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.api.util.Workaround;
import mod.moineau.contentpacks.block.statepredicates.StatePredicate;
import mod.moineau.contentpacks.event.ContentPacksEvents;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface EntityTypedStatePredicate extends BlockBehaviour.StateArgumentPredicate<EntityType<?>> {
	Codec<EntityTypedStatePredicate> BASE_CODEC = ContentRegistries.ENTITY_TYPED_STATE_PREDICATE_TYPE.byNameCodec()
			.dispatch(EntityTypedStatePredicate::getType, EntityTypedStatePredicateType::codec);
	Codec<EntityTypedStatePredicate> EXTENDED_CODEC = Codec.withAlternative(BASE_CODEC,
			DelegatingEntityTypedStatePredicate.CODEC.codec());
	@Workaround
	Codec<BlockBehaviour.StateArgumentPredicate<EntityType<?>>> DOWNGRADED_CODEC = CodecUtil.downgrade(EXTENDED_CODEC, true);

	EntityTypedStatePredicateType<?> getType();

	private static EntityTypedStatePredicate delegate(StatePredicate delegate) {
		return new DelegatingEntityTypedStatePredicate(delegate);
	}

	static EntityTypedStatePredicate allOf(List<EntityTypedStatePredicate> predicates) {
		return new AllOfEntityTypedStatePredicate(predicates);
	}

	static EntityTypedStatePredicate allOf(EntityTypedStatePredicate... predicates) {
		return allOf(List.of(predicates));
	}

	static EntityTypedStatePredicate bothOf(EntityTypedStatePredicate first, EntityTypedStatePredicate second) {
		return allOf(List.of(first, second));
	}

	static EntityTypedStatePredicate anyOf(List<EntityTypedStatePredicate> predicates) {
		return new AnyOfEntityTypedStatePredicate(predicates);
	}

	static EntityTypedStatePredicate anyOf(EntityTypedStatePredicate... predicates) {
		return anyOf(List.of(predicates));
	}

	static EntityTypedStatePredicate eitherOf(EntityTypedStatePredicate first, EntityTypedStatePredicate second) {
		return anyOf(List.of(first, second));
	}

	static EntityTypedStatePredicate matchingBlocks(Vec3i offset, List<Block> blocks) {
		return delegate(StatePredicate.matchingBlocks(offset, blocks));
	}

	static EntityTypedStatePredicate matchingBlocks(List<Block> blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static EntityTypedStatePredicate matchingBlocks(Vec3i offset, Block... blocks) {
		return matchingBlocks(offset, List.of(blocks));
	}

	static EntityTypedStatePredicate matchingBlocks(Block... blocks) {
		return matchingBlocks(Vec3i.ZERO, blocks);
	}

	static EntityTypedStatePredicate matchingBlockTag(Vec3i offset, TagKey<Block> tag) {
		return delegate(StatePredicate.matchingBlockTag(offset, tag));
	}

	static EntityTypedStatePredicate matchingBlockTag(TagKey<Block> offset) {
		return matchingBlockTag(Vec3i.ZERO, offset);
	}

	static EntityTypedStatePredicate matchingFluids(Vec3i offset, List<Fluid> fluids) {
		return delegate(StatePredicate.matchingFluids(offset, fluids));
	}

	static EntityTypedStatePredicate matchingFluids(Vec3i offset, Fluid... fluids) {
		return matchingFluids(offset, List.of(fluids));
	}

	static EntityTypedStatePredicate matchingFluids(Fluid... fluids) {
		return matchingFluids(Vec3i.ZERO, fluids);
	}

	static EntityTypedStatePredicate not(EntityTypedStatePredicate predicate) {
		return new NotEntityTypedStatePredicate(predicate);
	}

	static EntityTypedStatePredicate replaceable(Vec3i offset) {
		return delegate(StatePredicate.replaceable(offset));
	}

	static EntityTypedStatePredicate replaceable() {
		return replaceable(Vec3i.ZERO);
	}

	static EntityTypedStatePredicate hasSturdyFace(Vec3i offset, Direction face) {
		return delegate(StatePredicate.hasSturdyFace(offset, face));
	}

	static EntityTypedStatePredicate hasSturdyFace(Direction face) {
		return hasSturdyFace(Vec3i.ZERO, face);
	}

	static EntityTypedStatePredicate solid(Vec3i offset) {
		return delegate(StatePredicate.solid(offset));
	}

	static EntityTypedStatePredicate solid() {
		return solid(Vec3i.ZERO);
	}

	static EntityTypedStatePredicate noFluid() {
		return noFluid(Vec3i.ZERO);
	}

	static EntityTypedStatePredicate noFluid(Vec3i offset) {
		return matchingFluids(offset, Fluids.EMPTY);
	}

	static EntityTypedStatePredicate insideWorldBounds(Vec3i offset) {
		return delegate(StatePredicate.insideWorldBounds(offset));
	}

	static EntityTypedStatePredicate alwaysTrue() {
		return AlwaysEntityTypedStatePredicate.TRUE;
	}

	static EntityTypedStatePredicate alwaysFalse() {
		return AlwaysEntityTypedStatePredicate.TRUE;
	}

	static EntityTypedStatePredicate matchingProperties(Vec3i offset, Map<Property<?>, Comparison<?>> propertyMap) {
		return delegate(StatePredicate.matchingProperties(offset, propertyMap));
	}

	static EntityTypedStatePredicate matchingProperties(Map<Property<?>, Comparison<?>> propertyMap) {
		return matchingProperties(Vec3i.ZERO, propertyMap);
	}

	static <T extends Comparable<T>> EntityTypedStatePredicate matchingProperties(Vec3i offset, Property<T> property, Comparison<T> comparison) {
		return matchingProperties(offset, Map.of(property, comparison));
	}

	static <T extends Comparable<T>> EntityTypedStatePredicate matchingProperties(Property<T> property, Comparison<T> comparison) {
		return matchingProperties(Map.of(property, comparison));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> EntityTypedStatePredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> EntityTypedStatePredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> EntityTypedStatePredicate matchingProperties(Vec3i offset, Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(offset, Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> EntityTypedStatePredicate matchingProperties(Property<T1> property1, Comparison<T1> comparison1, Property<T2> property2, Comparison<T2> comparison2, Property<T3> property3, Comparison<T3> comparison3) {
		return matchingProperties(Map.of(property1, comparison1, property2, comparison2, property3, comparison3));
	}

	static EntityTypedStatePredicate isFullCube() {
		return delegate(StatePredicate.isFullCube());
	}

	static EntityTypedStatePredicate isSideSolidFullCube(Direction direction) {
		return delegate(StatePredicate.isSideSolidFullCube(direction));
	}

	static EntityTypedStatePredicate lightEmission(Comparison<Integer> predicate) {
		return lightEmission(Vec3i.ZERO, predicate);
	}

	static EntityTypedStatePredicate lightEmission(Vec3i offset, Comparison<Integer> predicate) {
		return delegate(StatePredicate.lightEmission(offset, predicate));
	}

	static EntityTypedStatePredicate blocksMovement() {
		return blocksMovement(Vec3i.ZERO);
	}

	static EntityTypedStatePredicate blocksMovement(Vec3i offset) {
		return delegate(StatePredicate.blocksMovement(offset));
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedStatePredicate whitelist(List<EntityType<?>> types) {
		return new WhitelistEntityTypedStatePredicate(types);
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedStatePredicate whitelist(EntityType<?>... types) {
		return whitelist(List.of(types));
	}

	@Workaround
	@ApiStatus.Internal
	static EntityTypedStatePredicate whitelist(Supplier<List<EntityType<?>>> types) {
		WhitelistEntityTypedStatePredicate predicate = new WhitelistEntityTypedStatePredicate(new ArrayList<>());
		ContentPacksEvents.REGISTRIES_LOADED.register(() -> predicate.list().addAll(types.get()));
		return predicate;
	}

	static EntityTypedStatePredicate isFireImmune() {
		return IsFireImmuneEntityTypedStatePredicate.INSTANCE;
	}
}
