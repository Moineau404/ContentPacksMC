package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mod.moineau.contentpacks.api.util.CodecUtil;
import mod.moineau.contentpacks.api.util.Workaround;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.AbstractBlock;

public interface BlockContextPredicate extends AbstractBlock.ContextPredicate {
	Codec<BlockContextPredicate> BASE_CODEC = ContentRegistries.BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatch(BlockContextPredicate::getType, BlockContextPredicateType::codec);
	MapCodec<BlockContextPredicate> MAP_CODEC = ContentRegistries.BLOCK_CONTEXT_PREDICATE_TYPE.getCodec()
			.dispatchMap(BlockContextPredicate::getType, BlockContextPredicateType::codec);
	@Workaround
	Codec<AbstractBlock.ContextPredicate> DOWNGRADED_CODEC = CodecUtil.downgrade(BASE_CODEC, true);

	BlockContextPredicateType<?> getType();

	// TODO Utility methods
}
