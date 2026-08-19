package mod.moineau.contentpacks.block.statepredicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public final class BlocksMotionStatePredicate extends OffsetContextPredicate {
    public static final MapCodec<BlocksMotionStatePredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).apply(instance, BlocksMotionStatePredicate::new)
    );

    BlocksMotionStatePredicate(Vec3i offset) {
        super(offset);
    }

    @Override
    public boolean test(BlockState state) {
        return state.blocksMotion();
    }

    @Override
    public StatePredicateType<?> getType() {
        return StatePredicateType.BLOCKS_MOTION;
    }
}
