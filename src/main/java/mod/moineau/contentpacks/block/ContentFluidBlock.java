package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.util.WIP;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

@WIP
public class ContentFluidBlock extends FluidBlock {
	public ContentFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	// TODO #canPathfindThrough

	@Override
	protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}

	// TODO Fluid interaction system
	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}
}
