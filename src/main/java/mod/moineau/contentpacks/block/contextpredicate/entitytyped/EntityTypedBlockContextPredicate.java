package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.block.Bakeable;
import mod.moineau.contentpacks.block.contextpredicate.BlockContextPredicate;
import mod.moineau.contentpacks.block.statepredicate.BlockStatePredicate;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface EntityTypedBlockContextPredicate extends AbstractBlock.TypedContextPredicate<EntityType<?>>, Bakeable {
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

	static EntityTypedBlockContextPredicate matchingState(BlockStatePredicate predicate) {
		return delegate(BlockContextPredicate.matchingState(predicate));
	}

	static EntityTypedBlockContextPredicate isFullCube() {
		return delegate(BlockContextPredicate.isFullCube());
	}

	static EntityTypedBlockContextPredicate isSideSolidFullCube(Direction direction) {
		return delegate(BlockContextPredicate.isSideSolidFullCube(direction));
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier, Event, Function)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedBlockContextPredicate whitelist(List<EntityType<?>> types) {
		return new WhitelistEntityTypedBlockContextPredicate(types);
	}

	/**
	 * @deprecated Use {@link #whitelist(Supplier, Event, Function)} instead.
	 * This is because Entity Types are loaded after blocks.
	 */
	@Deprecated
	static EntityTypedBlockContextPredicate whitelist(EntityType<?>... types) {
		return whitelist(List.of(types));
	}

	@Workaround
	@ApiStatus.Internal
	static <E> EntityTypedBlockContextPredicate whitelist(Supplier<List<EntityType<?>>> types, Event<E> event, Function<Runnable, E> callbackAdapter) {
		WhitelistEntityTypedBlockContextPredicate predicate = new WhitelistEntityTypedBlockContextPredicate(new ArrayList<>());
		event.register(callbackAdapter.apply(() -> predicate.list().addAll(types.get())));
		return predicate;
	}

	static EntityTypedBlockContextPredicate isFireImmune() {
		return IsFireImmuneEntityTypedBlockContextPredicate.INSTANCE;
	}
}
