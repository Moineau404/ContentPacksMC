package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;

public interface EntityTypedBlockContextPredicateType<P extends EntityTypedBlockContextPredicate> {
	EntityTypedBlockContextPredicateType<AnyOfEntityTypedBlockContextPredicate> ANY_OF = () -> AnyOfEntityTypedBlockContextPredicate.CODEC;
	EntityTypedBlockContextPredicateType<AllOfEntityTypedBlockContextPredicate> ALL_OF = () -> AllOfEntityTypedBlockContextPredicate.CODEC;
	EntityTypedBlockContextPredicateType<NotEntityTypedBlockContextPredicate> NOT = () -> NotEntityTypedBlockContextPredicate.CODEC;
	EntityTypedBlockContextPredicateType<AlwaysEntityTypedBlockContextPredicate> TRUE = () -> AlwaysEntityTypedBlockContextPredicate.TRUE_CODEC;
	EntityTypedBlockContextPredicateType<AlwaysEntityTypedBlockContextPredicate> FALSE = () -> AlwaysEntityTypedBlockContextPredicate.FALSE_CODEC;
	EntityTypedBlockContextPredicateType<WhitelistEntityTypedBlockContextPredicate> WHITELIST = () -> WhitelistEntityTypedBlockContextPredicate.CODEC;
	EntityTypedBlockContextPredicateType<AlwaysEntityTypedBlockContextPredicate> IS_FIRE_IMMUNE = () -> AlwaysEntityTypedBlockContextPredicate.FALSE_CODEC;

	MapCodec<? extends P> codec();

	static void initialize(Registry<EntityTypedBlockContextPredicateType<?>> registry) {
		Registry.register(registry, "any_of", ANY_OF);
		Registry.register(registry, "all_of", ALL_OF);
		Registry.register(registry, "not", NOT);
		Registry.register(registry, "true", TRUE);
		Registry.register(registry, "false", FALSE);
		Registry.register(registry, "whitelist", WHITELIST);
		Registry.register(registry, "is_fire_immune", IS_FIRE_IMMUNE);

	}
}
