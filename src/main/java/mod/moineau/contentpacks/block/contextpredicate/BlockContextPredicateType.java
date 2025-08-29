package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.ContentPacks;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface BlockContextPredicateType<P extends BlockContextPredicate> {
	BlockContextPredicateType<MatchingBlocksBlockContextPredicate> MATCHING_BLOCKS = () -> MatchingBlocksBlockContextPredicate.CODEC;
	BlockContextPredicateType<MatchingBlockTagContextPredicate> MATCHING_BLOCK_TAG = () -> MatchingBlockTagContextPredicate.CODEC;
	BlockContextPredicateType<MatchingFluidsBlockContextPredicate> MATCHING_FLUIDS = () -> MatchingFluidsBlockContextPredicate.CODEC;
	BlockContextPredicateType<HasSturdyFaceContextPredicate> HAS_STURDY_FACE = () -> HasSturdyFaceContextPredicate.CODEC;
	BlockContextPredicateType<SolidBlockContextPredicate> SOLID = () -> SolidBlockContextPredicate.CODEC;
	BlockContextPredicateType<ReplaceableBlockContextPredicate> REPLACEABLE = () -> ReplaceableBlockContextPredicate.CODEC;
	BlockContextPredicateType<InsideWorldBoundsBlockContextPredicate> INSIDE_WORLD_BOUNDS = () -> InsideWorldBoundsBlockContextPredicate.CODEC;
	BlockContextPredicateType<AnyOfBlockContextPredicate> ANY_OF = () -> AnyOfBlockContextPredicate.CODEC;
	BlockContextPredicateType<AllOfBlockContextPredicate> ALL_OF = () -> AllOfBlockContextPredicate.CODEC;
	BlockContextPredicateType<NotBlockContextPredicate> NOT = () -> NotBlockContextPredicate.CODEC;
	BlockContextPredicateType<AlwaysBlockContextPredicate> TRUE = AlwaysBlockContextPredicate.TRUE;
	BlockContextPredicateType<AlwaysBlockContextPredicate> FALSE = AlwaysBlockContextPredicate.FALSE;
	BlockContextPredicateType<MatchingPropertiesBlockContextPredicate> MATCHING_PROPERTIES = () -> MatchingPropertiesBlockContextPredicate.CODEC;
	BlockContextPredicateType<IsSideSolidFullCubeBlockContextPredicate> IS_SIDE_SOLID_FULL_CUBE = () -> IsSideSolidFullCubeBlockContextPredicate.CODEC;
	BlockContextPredicateType<IsFullCubeBlockContextPredicate> IS_FULL_CUBE = () -> IsFullCubeBlockContextPredicate.CODEC;
	BlockContextPredicateType<BlocksMovementBlockContextPredicate> BLOCKS_MOVEMENT = () -> BlocksMovementBlockContextPredicate.CODEC;
	BlockContextPredicateType<BlocksMovementBlockContextPredicate> LUMINANCE = () -> BlocksMovementBlockContextPredicate.CODEC;

	MapCodec<P> codec();

	static void initialize(Registry<BlockContextPredicateType<?>> registry) {
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
		Registry.register(registry, Identifier.of(ContentPacks.MOD_ID, "matching_properties"), MATCHING_PROPERTIES);
		Registry.register(registry, "is_side_solid_full_cube", IS_SIDE_SOLID_FULL_CUBE);
		Registry.register(registry, "is_full_cube", IS_FULL_CUBE);
		Registry.register(registry, "blocks_movement", BLOCKS_MOVEMENT);
		Registry.register(registry, "luminance", LUMINANCE);

	}
}
