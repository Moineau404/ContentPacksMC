package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3i;

@Deprecated
public class SolidBlockContextPredicate extends OffsetContextPredicate {
	public static final MapCodec<SolidBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance).apply(instance, SolidBlockContextPredicate::new)
	);

	public SolidBlockContextPredicate(Vec3i vec3i) {
		super(vec3i);
	}

	@Override
	protected boolean test(BlockState state) {
		return state.isSolid();
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.SOLID;
	}
}
