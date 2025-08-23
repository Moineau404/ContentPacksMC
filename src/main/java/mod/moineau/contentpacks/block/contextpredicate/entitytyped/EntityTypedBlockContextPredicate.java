package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.EntityType;

public interface EntityTypedBlockContextPredicate extends AbstractBlock.TypedContextPredicate<EntityType<?>> {
	Codec<EntityTypedBlockContextPredicate> BASE_CODEC = ContentRegistries.ENTITY_TYPED_BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatch(EntityTypedBlockContextPredicate::getType, EntityTypedBlockContextPredicateType::codec);
	Codec<EntityTypedBlockContextPredicate> EXTENDED_CODEC = Codec.withAlternative(BASE_CODEC,
			ContextEntityTypedBlockContextPredicate.CODEC.codec());
	@Workaround
	Codec<AbstractBlock.TypedContextPredicate<EntityType<?>>> DOWNGRADED_CODEC = CodecUtil.downgrade(EXTENDED_CODEC, true);

	EntityTypedBlockContextPredicateType<?> getType();
}
