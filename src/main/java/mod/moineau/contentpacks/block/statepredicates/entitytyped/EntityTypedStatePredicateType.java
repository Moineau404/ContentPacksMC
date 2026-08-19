package mod.moineau.contentpacks.block.statepredicates.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public interface EntityTypedStatePredicateType<P extends EntityTypedStatePredicate> {
	EntityTypedStatePredicateType<AnyOfEntityTypedStatePredicate> ANY_OF = () -> AnyOfEntityTypedStatePredicate.CODEC;
	EntityTypedStatePredicateType<AllOfEntityTypedStatePredicate> ALL_OF = () -> AllOfEntityTypedStatePredicate.CODEC;
	EntityTypedStatePredicateType<NotEntityTypedStatePredicate> NOT = () -> NotEntityTypedStatePredicate.CODEC;
	EntityTypedStatePredicateType<AlwaysEntityTypedStatePredicate> TRUE = () -> AlwaysEntityTypedStatePredicate.TRUE_CODEC;
	EntityTypedStatePredicateType<AlwaysEntityTypedStatePredicate> FALSE = () -> AlwaysEntityTypedStatePredicate.FALSE_CODEC;
	EntityTypedStatePredicateType<WhitelistEntityTypedStatePredicate> WHITELIST = () -> WhitelistEntityTypedStatePredicate.CODEC;
	EntityTypedStatePredicateType<IsFireImmuneEntityTypedStatePredicate> IS_FIRE_IMMUNE = () -> IsFireImmuneEntityTypedStatePredicate.CODEC;

	MapCodec<? extends P> codec();

	static void initialize(Registry<EntityTypedStatePredicateType<?>> registry) {
		Registry.register(registry, "any_of", ANY_OF);
		Registry.register(registry, "all_of", ALL_OF);
		Registry.register(registry, "not", NOT);
		Registry.register(registry, "true", TRUE);
		Registry.register(registry, "false", FALSE);
		Registry.register(registry, "whitelist", WHITELIST);
		Registry.register(registry, "is_fire_immune", IS_FIRE_IMMUNE);

	}
}
