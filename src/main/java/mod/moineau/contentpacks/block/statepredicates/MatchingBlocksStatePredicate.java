package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MatchingBlocksStatePredicate extends OffsetContextPredicate {
	private final HolderSet<Block> blocks;
	public static final MapCodec<MatchingBlocksStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance)
			.and(RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(predicate -> predicate.blocks))
			.apply(instance, MatchingBlocksStatePredicate::new)
	);

	public MatchingBlocksStatePredicate(Vec3i offset, HolderSet<Block> blocks) {
		super(offset);
		this.blocks = blocks;
	}

	@Override
	protected boolean test(BlockState state) {
		return state.is(this.blocks);
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.MATCHING_BLOCKS;
	}
}
