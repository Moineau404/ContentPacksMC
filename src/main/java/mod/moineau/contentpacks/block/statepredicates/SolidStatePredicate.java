package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated
public class SolidStatePredicate extends OffsetContextPredicate {
	public static final MapCodec<SolidStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance).apply(instance, SolidStatePredicate::new)
	);

	public SolidStatePredicate(Vec3i vec3i) {
		super(vec3i);
	}

	@Override
	protected boolean test(BlockState state) {
		return state.isSolid();
	}

	@Override
	public StatePredicateType<?> getType() {
		return StatePredicateType.SOLID;
	}
}
