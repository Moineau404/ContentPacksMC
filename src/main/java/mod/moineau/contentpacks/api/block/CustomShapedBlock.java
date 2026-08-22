package mod.moineau.contentpacks.api.block;

import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.moineau.api.util.CodecUtil;
import mod.moineau.contentpacks.codec.ShapeCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class CustomShapedBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<CustomShapedBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ShapeCodecs.CODEC.fieldOf("shape").forGetter(block -> block.shape),
            FacingType.CODEC.optionalFieldOf("facing_type", FacingType.NONE).forGetter(block -> block.facingType),
            Codec.BOOL.optionalFieldOf("waterloggable", false).forGetter(block -> block.waterloggable),
            propertiesCodec()
    ).apply(instance, CustomShapedBlock::new));
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final EnumProperty<Direction> HORIZONTAL_FACING = HorizontalDirectionalBlock.FACING;

    private final VoxelShape shape;
    private final FacingType facingType;
    private final boolean waterloggable;

    public CustomShapedBlock(VoxelShape shape, FacingType facingType, boolean waterloggable, Properties properties) {
        this.shape = shape;
        this.facingType = facingType;
        this.waterloggable = waterloggable;
        super(properties);
        BlockState defaultState = this.defaultBlockState();
        switch (this.facingType) {
            case HORIZONTAL -> defaultState.setValue(HORIZONTAL_FACING, Direction.NORTH);
            case ALL -> defaultState.setValue(FACING, Direction.NORTH);
            default ->  {}
        }
        if (this.waterloggable) {
            defaultState.setValue(WATERLOGGED, false);
        }
        this.registerDefaultState(defaultState);
    }

    public static MapCodec<CustomShapedBlock> createPreset(VoxelShape shape, FacingType facingType, boolean waterloggable) {
        return simpleCodec(properties -> new CustomShapedBlock(shape, facingType, waterloggable, properties));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        switch (this.facingType) {
            case HORIZONTAL -> builder.add(HORIZONTAL_FACING);
            case ALL -> builder.add(FACING);
            default ->  {}
        }
        if (this.waterloggable) {
            builder.add(WATERLOGGED);
        }
    }

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return switch (this.facingType) {
            case HORIZONTAL -> Shapes.rotate(this.shape, this.facingType.getRotation(state.getValue(HORIZONTAL_FACING)));
            case ALL -> Shapes.rotate(this.shape, this.facingType.getRotation(state.getValue(FACING)));
            default ->  this.shape;
        };
    }

    @Override
    protected BlockState updateShape(
            final BlockState state,
            final LevelReader level,
            final ScheduledTickAccess ticks,
            final BlockPos pos,
            final Direction directionToNeighbour,
            final BlockPos neighbourPos,
            final BlockState neighbourState,
            final RandomSource random
    ) {
        if (this.waterloggable && state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean useShapeForLightOcclusion(final BlockState state) {
        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState replacedFluidState = context.getLevel().getFluidState(pos);
        BlockState state = this.defaultBlockState();
        switch (this.facingType) {
            case HORIZONTAL -> state = state.setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
            case ALL -> state = state.setValue(FACING, context.getNearestLookingDirection().getOpposite());
            default ->  {}
        }
        if (this.waterloggable) {
            state = state.setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
        }
        return state;
    }

    @Override
    protected FluidState getFluidState(final BlockState state) {
        if (this.waterloggable && state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(final LevelAccessor level, final BlockPos pos, final BlockState state, final FluidState fluidState) {
        return this.waterloggable && SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public ItemStack pickupBlock(final @Nullable LivingEntity user, final LevelAccessor level, final BlockPos pos, final BlockState state) {
        return this.waterloggable ? SimpleWaterloggedBlock.super.pickupBlock(user, level, pos, state) : ItemStack.EMPTY;
    }

    @Override
    protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return switch (type) {
            case LAND -> false;
            case WATER -> state.getFluidState().is(FluidTags.WATER);
            case AIR -> false;
        };
    }

    public enum FacingType {
        NONE {
            @Override
            public OctahedralGroup getRotation(Direction direction) {
                return OctahedralGroup.IDENTITY;
            }
        },
        HORIZONTAL,
        ALL;

        public static Codec<FacingType> CODEC = CodecUtil.enumByName(FacingType.class);
        public OctahedralGroup getRotation(Direction direction) {
            return switch (direction) {
                case DOWN -> OctahedralGroup.ROT_90_X_NEG;
                case UP -> OctahedralGroup.ROT_90_X_POS;
                case NORTH -> OctahedralGroup.IDENTITY;
                case SOUTH -> OctahedralGroup.ROT_180_FACE_XZ;
                case WEST -> OctahedralGroup.ROT_90_Y_POS;
                case EAST -> OctahedralGroup.ROT_90_Y_NEG;
            };
        }
    }
}
