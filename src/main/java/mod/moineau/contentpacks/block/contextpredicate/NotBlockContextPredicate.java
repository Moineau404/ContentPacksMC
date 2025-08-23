package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class NotBlockContextPredicate implements BlockContextPredicate {
	public static final MapCodec<NotBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate))
			.apply(instance, NotBlockContextPredicate::new)
	);
	private final BlockContextPredicate predicate;

	public NotBlockContextPredicate(BlockContextPredicate predicate) {
		this.predicate = predicate;
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos) {
		return !this.predicate.test(state, world, pos);
	}

	@Override
	public BlockContextPredicateType<?> getType() {
		return BlockContextPredicateType.NOT;
	}
}
