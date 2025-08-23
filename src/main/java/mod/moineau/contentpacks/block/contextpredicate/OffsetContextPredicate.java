package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.datafixers.Products.P1;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

public abstract class OffsetContextPredicate implements BlockContextPredicate {
	protected final Vec3i offset;

	protected static <P extends OffsetContextPredicate> P1<Mu<P>, Vec3i> registerOffsetField(Instance<P> instance) {
		return instance.group(Vec3i.createOffsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(predicate -> predicate.offset));
	}

	protected OffsetContextPredicate(Vec3i offset) {
		this.offset = offset;
	}

	public final boolean test(BlockState state, BlockView world, BlockPos pos) {
		return this.test(world.getBlockState(pos.add(this.offset)));
	}

	protected abstract boolean test(BlockState state);
}
