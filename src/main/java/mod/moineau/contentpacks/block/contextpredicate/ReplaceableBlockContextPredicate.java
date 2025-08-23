package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3i;

public class ReplaceableBlockContextPredicate extends OffsetContextPredicate {
	public static final MapCodec<ReplaceableBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> registerOffsetField(instance).apply(instance, ReplaceableBlockContextPredicate::new)
	);

	public ReplaceableBlockContextPredicate(Vec3i vec3i) {
		super(vec3i);
	}

	@Override
	protected boolean test(BlockState state) {
		return state.isReplaceable();
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.REPLACEABLE;
	}
}
