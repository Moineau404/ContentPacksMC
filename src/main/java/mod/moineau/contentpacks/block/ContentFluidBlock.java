package mod.moineau.contentpacks.block;

import mod.moineau.contentpacks.api.util.WIP;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

@WIP
public class ContentFluidBlock extends LiquidBlock {
	public ContentFluidBlock(FlowingFluid fluid, Properties settings) {
		super(fluid, settings);
	}

	// TODO #canPathfindThrough

	@Override
	protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
	}

	// TODO Fluid interaction system
	@Override
	protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
		world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
	}
}
