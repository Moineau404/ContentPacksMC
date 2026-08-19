package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.datafixers.Products.P1;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OffsetContextPredicate implements StatePredicate {
	protected final Vec3i offset;

	protected static <P extends OffsetContextPredicate> P1<Mu<P>, Vec3i> registerOffsetField(Instance<P> instance) {
		return instance.group(Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(predicate -> predicate.offset));
	}

	protected OffsetContextPredicate(Vec3i offset) {
		this.offset = offset;
	}

	public final boolean test(BlockState state, BlockGetter world, BlockPos pos) {
		return this.test(world.getBlockState(pos.offset(this.offset)));
	}

	protected abstract boolean test(BlockState state);
}
