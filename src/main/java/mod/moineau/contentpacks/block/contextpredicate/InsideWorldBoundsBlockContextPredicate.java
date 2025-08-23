package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

public class InsideWorldBoundsBlockContextPredicate implements BlockContextPredicate {
	public static final MapCodec<InsideWorldBoundsBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", BlockPos.ORIGIN).forGetter(predicate -> predicate.offset))
			.apply(instance, InsideWorldBoundsBlockContextPredicate::new)
	);
	private final Vec3i offset;

	public InsideWorldBoundsBlockContextPredicate(Vec3i offset) {
		this.offset = offset;
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos) {
		return !world.isOutOfHeightLimit(pos.add(this.offset));
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.INSIDE_WORLD_BOUNDS;
	}
}
