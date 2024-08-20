package dev.spiritstudios.hollow.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class HollowLogBlock extends PillarBlock implements Waterloggable {
    public static final VoxelShape SHAPE_X = VoxelShapes.union(
            createCuboidShape(0, 14, 0, 16, 16, 16),
            createCuboidShape(0, 0, 2, 16, 2, 14),
            createCuboidShape(0, 0, 0, 16, 14, 2),
            createCuboidShape(0, 0, 14, 16, 14, 16)
    );
    
    public static final VoxelShape SHAPE_Y = VoxelShapes.union(
            createCuboidShape(0, 0, 0, 2, 16, 16),
            createCuboidShape(14, 0, 2, 16, 16, 14),
            createCuboidShape(2, 0, 0, 16, 16, 2),
            createCuboidShape(2, 0, 14, 16, 16, 16)
    );
    
    public static final VoxelShape SHAPE_Z = VoxelShapes.union(
            createCuboidShape(0, 14, 0, 16, 16, 16),
            createCuboidShape(2, 0, 0, 14, 2, 16),
            createCuboidShape(0, 0, 0, 2, 14, 16),
            createCuboidShape(14, 0, 0, 16, 14, 16)
    );
    
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty MOSSY = BooleanProperty.of("mossy");
    
    public String insideTexture;
    public String sideTexture;
    public String endTexture;
    
    public HollowLogBlock(Settings settings, String sideTexture, String insideTexture, String endTexture) {
        super(settings);
        this.insideTexture = insideTexture;
        this.sideTexture = sideTexture;
        this.endTexture = endTexture;
        setDefaultState(getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(WATERLOGGED, false)
                .with(MOSSY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.AXIS, WATERLOGGED, MOSSY);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return !state.get(WATERLOGGED) && state.get(AXIS) == Direction.Axis.Y;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Block above = ctx.getWorld().getBlockState(ctx.getBlockPos().up()).getBlock();
        return this.getDefaultState()
                .with(Properties.AXIS, ctx.getSide().getAxis())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER))
                .with(MOSSY, (above == Blocks.MOSS_CARPET) || (above == Blocks.MOSS_BLOCK));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        
        if (direction == Direction.UP) {
            Block above = neighborState.getBlock();
            return state.with(MOSSY, (above == Blocks.MOSS_CARPET) || (above == Blocks.MOSS_BLOCK));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> SHAPE_X;
            case Y -> SHAPE_Y;
            default -> SHAPE_Z;
        };
    }
}
