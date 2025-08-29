package mod.moineau.contentpacks.block.contextpredicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3i;

public final class BlocksMovementBlockContextPredicate extends OffsetContextPredicate {
    public static final MapCodec<BlocksMovementBlockContextPredicate> CODEC = RecordCodecBuilder.mapCodec(
            instance -> registerOffsetField(instance).apply(instance, BlocksMovementBlockContextPredicate::new)
    );

    BlocksMovementBlockContextPredicate(Vec3i offset) {
        super(offset);
    }

    @Override
    public boolean test(BlockState state) {
        return state.blocksMovement();
    }

    @Override
    public BlockContextPredicateType<?> getType() {
        return BlockContextPredicateType.BLOCKS_MOVEMENT;
    }
}
