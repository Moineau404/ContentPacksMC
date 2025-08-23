package mod.moineau.contentpacks.block.contextpredicate.entitytyped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class NotEntityTypedBlockContextPredicate implements EntityTypedBlockContextPredicate {
	public static final MapCodec<NotEntityTypedBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate))
			.apply(instance, NotEntityTypedBlockContextPredicate::new)
	);
	private final EntityTypedBlockContextPredicate predicate;

	public NotEntityTypedBlockContextPredicate(EntityTypedBlockContextPredicate predicate) {
		this.predicate = predicate;
	}

	public boolean test(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return !this.predicate.test(state, world, pos, type);
	}

	@Override
	public EntityTypedBlockContextPredicateType<?> getType() {
		return EntityTypedBlockContextPredicateType.NOT;
	}
}
