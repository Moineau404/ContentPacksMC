package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.Vec3i;

public class MatchingBlocksBlockContextPredicate extends OffsetContextPredicate {
	private final RegistryEntryList<Block> blocks;
	public static final MapCodec<MatchingBlocksBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance)
			.and(RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("blocks").forGetter(predicate -> predicate.blocks))
			.apply(instance, MatchingBlocksBlockContextPredicate::new)
	);

	public MatchingBlocksBlockContextPredicate(Vec3i offset, RegistryEntryList<Block> blocks) {
		super(offset);
		this.blocks = blocks;
	}

	@Override
	protected boolean test(BlockState state) {
		return state.isIn(this.blocks);
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.MATCHING_BLOCKS;
	}
}
