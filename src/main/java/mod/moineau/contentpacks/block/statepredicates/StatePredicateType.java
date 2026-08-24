package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.core.Registry;

public interface StatePredicateType<P extends StatePredicate> {
	StatePredicateType<MatchingBlocksStatePredicate> MATCHING_BLOCKS = () -> MatchingBlocksStatePredicate.CODEC;
	StatePredicateType<MatchingStatePredicate> MATCHING_BLOCK_TAG = () -> MatchingStatePredicate.CODEC;
	StatePredicateType<MatchingFluidsStatePredicate> MATCHING_FLUIDS = () -> MatchingFluidsStatePredicate.CODEC;
	StatePredicateType<HasSturdyFaceContextPredicate> HAS_STURDY_FACE = () -> HasSturdyFaceContextPredicate.CODEC;
	StatePredicateType<SolidStatePredicate> SOLID = () -> SolidStatePredicate.CODEC;
	StatePredicateType<ReplaceableStatePredicate> REPLACEABLE = () -> ReplaceableStatePredicate.CODEC;
	StatePredicateType<InsideWorldBoundsStatePredicate> INSIDE_WORLD_BOUNDS = () -> InsideWorldBoundsStatePredicate.CODEC;
	StatePredicateType<AnyOfStatePredicate> ANY_OF = () -> AnyOfStatePredicate.CODEC;
	StatePredicateType<AllOfStatePredicate> ALL_OF = () -> AllOfStatePredicate.CODEC;
	StatePredicateType<NotStatePredicate> NOT = () -> NotStatePredicate.CODEC;
	StatePredicateType<AlwaysStatePredicate> TRUE = AlwaysStatePredicate.TRUE;
	StatePredicateType<AlwaysStatePredicate> FALSE = AlwaysStatePredicate.FALSE;
	StatePredicateType<MatchingPropertiesStatePredicate> MATCHING_PROPERTIES = () -> MatchingPropertiesStatePredicate.CODEC;
	StatePredicateType<IsFaceSturdyStatePredicate> IS_FACE_STURDY = () -> IsFaceSturdyStatePredicate.CODEC;
	StatePredicateType<IsCollisionShapeFullBlockStatePredicate> IS_COLLISION_SHAPE_FULL_BLOCK = () -> IsCollisionShapeFullBlockStatePredicate.CODEC;
	StatePredicateType<BlocksMotionStatePredicate> BLOCKS_MOTION = () -> BlocksMotionStatePredicate.CODEC;
	StatePredicateType<LightEmissionStatePredicate> LIGHT_EMISSION = () -> LightEmissionStatePredicate.CODEC;

	MapCodec<P> codec();

	static void initialize(Registry<StatePredicateType<?>> registry) {
		Registry.register(registry, "matching_blocks", MATCHING_BLOCKS);
		Registry.register(registry, "matching_block_tag", MATCHING_BLOCK_TAG);
		Registry.register(registry, "matching_fluids", MATCHING_FLUIDS);
		Registry.register(registry, "has_sturdy_face", HAS_STURDY_FACE);
		Registry.register(registry, "solid", SOLID);
		Registry.register(registry, "replaceable", REPLACEABLE);
		Registry.register(registry, "inside_world_bounds", INSIDE_WORLD_BOUNDS);
		Registry.register(registry, "any_of", ANY_OF);
		Registry.register(registry, "all_of", ALL_OF);
		Registry.register(registry, "not", NOT);
		Registry.register(registry, "true", TRUE);
		Registry.register(registry, "false", FALSE);
		Registry.register(registry, ContentPacks.id("matching_properties"), MATCHING_PROPERTIES);
		Registry.register(registry, "is_face_sturdy", IS_FACE_STURDY);
		Registry.register(registry, "is_collision_shape_full_block", IS_COLLISION_SHAPE_FULL_BLOCK);
		Registry.register(registry, "blocks_motion", BLOCKS_MOTION);
		Registry.register(registry, "light_emission", LIGHT_EMISSION);

	}
}
