package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class NotStatePredicate implements StatePredicate {
	public static final MapCodec<NotStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate))
			.apply(instance, NotStatePredicate::new)
	);
	private final StatePredicate predicate;

	public NotStatePredicate(StatePredicate predicate) {
		this.predicate = predicate;
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
		return !this.predicate.test(state, world, pos);
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.NOT;
	}
}
