package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

public class HasSturdyFaceContextPredicate implements BlockContextPredicate {
	private final Vec3i offset;
	private final Direction face;
	public static final MapCodec<HasSturdyFaceContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Vec3i.createOffsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(predicate -> predicate.offset),
				Direction.CODEC.fieldOf("direction").forGetter(predicate -> predicate.face)
			)
			.apply(instance, HasSturdyFaceContextPredicate::new)
	);

	public HasSturdyFaceContextPredicate(Vec3i offset, Direction face) {
		this.offset = offset;
		this.face = face;
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos) {
		BlockPos blockPos2 = pos.add(this.offset);
		return world.getBlockState(blockPos2).isSideSolidFullSquare(world, blockPos2, this.face);
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.HAS_STURDY_FACE;
	}
}
