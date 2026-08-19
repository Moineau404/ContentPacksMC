package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class HasSturdyFaceContextPredicate implements StatePredicate {
	private final Vec3i offset;
	private final Direction face;
	public static final MapCodec<HasSturdyFaceContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(predicate -> predicate.offset),
				Direction.CODEC.fieldOf("direction").forGetter(predicate -> predicate.face)
			)
			.apply(instance, HasSturdyFaceContextPredicate::new)
	);

	public HasSturdyFaceContextPredicate(Vec3i offset, Direction face) {
		this.offset = offset;
		this.face = face;
	}

	public boolean test(BlockState state, BlockGetter world, BlockPos pos) {
		BlockPos blockPos2 = pos.offset(this.offset);
		return world.getBlockState(blockPos2).isFaceSturdy(world, blockPos2, this.face);
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.HAS_STURDY_FACE;
	}
}
