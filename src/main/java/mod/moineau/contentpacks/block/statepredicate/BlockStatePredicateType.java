package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.registry.Registry;

public interface BlockStatePredicateType<P extends BlockStatePredicate> {
	BlockStatePredicateType<AnyOfBlockStatePredicate> ANY_OF = () -> AnyOfBlockStatePredicate.CODEC;
	BlockStatePredicateType<AllOfBlockStatePredicate> ALL_OF = () -> AllOfBlockStatePredicate.CODEC;
	BlockStatePredicateType<NotBlockStatePredicate> NOT = () -> NotBlockStatePredicate.CODEC;
	BlockStatePredicateType<AlwaysBlockStatePredicate> TRUE = () -> AlwaysBlockStatePredicate.TRUE_CODEC;
	BlockStatePredicateType<AlwaysBlockStatePredicate> FALSE = () -> AlwaysBlockStatePredicate.FALSE_CODEC;
	BlockStatePredicateType<MatchingPropertiesBlockStatePredicate> MATCHING_PROPERTIES = () -> MatchingPropertiesBlockStatePredicate.CODEC;
	BlockStatePredicateType<LuminanceBlockStatePredicate> LUMINANCE = () -> LuminanceBlockStatePredicate.CODEC;
	BlockStatePredicateType<BlocksMovementBlockStatePredicate> BLOCKS_MOVEMENT = () -> BlocksMovementBlockStatePredicate.CODEC;

	MapCodec<P> codec();

	private static <P extends BlockStatePredicate> BlockStatePredicateType<P> register(String id, MapCodec<P> codec) {
		return Registry.register(ContentRegistries.BLOCK_STATE_PREDICATE_TYPE, id, () -> codec);
	}

	static void initialize(Registry<BlockStatePredicateType<?>> registry) {
		Registry.register(registry, "matching_properties", MATCHING_PROPERTIES);
		Registry.register(registry, "luminance", LUMINANCE);
		Registry.register(registry, "blocks_movement", BLOCKS_MOVEMENT);
	}
}
