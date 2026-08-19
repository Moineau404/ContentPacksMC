package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MatchingStatePredicate extends OffsetContextPredicate {
	final TagKey<Block> tag;
	public static final MapCodec<MatchingStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance)
			.and(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(predicate -> predicate.tag))
			.apply(instance, MatchingStatePredicate::new)
	);

	protected MatchingStatePredicate(Vec3i offset, TagKey<Block> tag) {
		super(offset);
		this.tag = tag;
	}

	@Override
	protected boolean test(BlockState state) {
		return state.is(this.tag);
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.MATCHING_BLOCK_TAG;
	}
}
