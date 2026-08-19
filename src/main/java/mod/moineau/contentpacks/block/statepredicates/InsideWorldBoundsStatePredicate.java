package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class InsideWorldBoundsStatePredicate implements StatePredicate {
	public static final MapCodec<InsideWorldBoundsStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(Vec3i.offsetCodec(16).optionalFieldOf("offset", BlockPos.ZERO).forGetter(predicate -> predicate.offset))
			.apply(instance, InsideWorldBoundsStatePredicate::new)
	);
	private final Vec3i offset;

	public InsideWorldBoundsStatePredicate(Vec3i offset) {
		this.offset = offset;
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
		return !world.isOutsideBuildHeight(pos.offset(this.offset));
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.INSIDE_WORLD_BOUNDS;
	}
}
