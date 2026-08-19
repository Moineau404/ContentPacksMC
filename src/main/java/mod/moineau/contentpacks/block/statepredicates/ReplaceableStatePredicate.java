package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class ReplaceableStatePredicate extends OffsetContextPredicate {
	public static final MapCodec<ReplaceableStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance).apply(instance, ReplaceableStatePredicate::new)
	);

	public ReplaceableStatePredicate(Vec3i vec3i) {
		super(vec3i);
	}

	@Override
	protected boolean test(BlockState state) {
		return state.canBeReplaced();
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.REPLACEABLE;
	}
}
