package mod.moineau.contentpacks.block.statepredicate;

import com.mojang.serialization.Codec;
import mod.moineau.contentpacks.registry.ContentRegistries;
import net.minecraft.block.BlockState;

import java.util.function.Predicate;

public interface BlockStatePredicate extends Predicate<BlockState> {
	Codec<BlockStatePredicate> BASE_CODEC = ContentRegistries.BLOCK_STATE_PREDICATE_TYPE.getCodec()
			.dispatch(BlockStatePredicate::getType, BlockStatePredicateType::codec);

	BlockStatePredicateType<?> getType();
}
