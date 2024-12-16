package dev.spiritstudios.hollow.block;

import dev.spiritstudios.specter.api.core.math.VoxelShapeHelper;
import net.minecraft.block.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class StoneChestLidBlock extends Block {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<ChestType> CHEST_TYPE = Properties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    // region VoxelShapes
    public static final VoxelShape SHAPE_SINGLE = Block.createCuboidShape(1, 0, 1, 15, 4, 15);
    
    public static final VoxelShape SHAPE_LEFT_NORTH = Block.createCuboidShape(1, 0, 1, 16, 4, 15);
    public static final VoxelShape SHAPE_LEFT_EAST = VoxelShapeHelper.rotateHorizontal(Direction.EAST, Direction.NORTH, SHAPE_LEFT_NORTH);
    public static final VoxelShape SHAPE_LEFT_SOUTH = VoxelShapeHelper.rotateHorizontal(Direction.SOUTH, Direction.NORTH, SHAPE_LEFT_NORTH);
    public static final VoxelShape SHAPE_LEFT_WEST = VoxelShapeHelper.rotateHorizontal(Direction.WEST, Direction.NORTH, SHAPE_LEFT_NORTH);
    
    public static final VoxelShape SHAPE_RIGHT_NORTH = Block.createCuboidShape(0, 0, 1, 15, 4, 15);
    public static final VoxelShape SHAPE_RIGHT_EAST = VoxelShapeHelper.rotateHorizontal(Direction.EAST, Direction.NORTH, SHAPE_RIGHT_NORTH);
    public static final VoxelShape SHAPE_RIGHT_SOUTH = VoxelShapeHelper.rotateHorizontal(Direction.SOUTH, Direction.NORTH, SHAPE_RIGHT_NORTH);
    public static final VoxelShape SHAPE_RIGHT_WEST = VoxelShapeHelper.rotateHorizontal(Direction.WEST, Direction.NORTH, SHAPE_RIGHT_NORTH);
    // endregion
    
    public StoneChestLidBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CHEST_TYPE, ChestType.SINGLE)
                .with(WATERLOGGED, false)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        ChestType chestType = ChestType.SINGLE;
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean shouldCancel = ctx.shouldCancelInteraction();
        Direction side = ctx.getSide();

        if (side.getAxis().isHorizontal() && shouldCancel) {
            Direction neighborDirection = this.getNeighborChestDirection(ctx, side.getOpposite());
            if (neighborDirection != null && neighborDirection.getAxis() != side.getAxis()) {
                direction = neighborDirection;
                chestType = neighborDirection.rotateYCounterclockwise() == side.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chestType == ChestType.SINGLE && !shouldCancel)
            if (direction == this.getNeighborChestDirection(ctx, direction.rotateYClockwise()))
                chestType = ChestType.LEFT;
            else if (direction == this.getNeighborChestDirection(ctx, direction.rotateYCounterclockwise()))
                chestType = ChestType.RIGHT;

        return this.getDefaultState()
                .with(FACING, direction)
                .with(CHEST_TYPE, chestType)
                .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Nullable
    private Direction getNeighborChestDirection(ItemPlacementContext ctx, Direction dir) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(dir));
        return blockState.isOf(this) && blockState.get(CHEST_TYPE) == ChestType.SINGLE ? blockState.get(FACING) : null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) { return state.with(FACING, rotation.rotate(state.get(FACING))); }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) { return state.rotate(mirror.getRotation(state.get(FACING))); }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.get(WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        if (!neighborState.isOf(this) || !direction.getAxis().isHorizontal()) {
            if (getFacing(state) == direction) return state.with(CHEST_TYPE, ChestType.SINGLE);
        } else {
            ChestType chestType = neighborState.get(CHEST_TYPE);
            if (state.get(CHEST_TYPE) == ChestType.SINGLE
                    && chestType != ChestType.SINGLE
                    && state.get(FACING) == neighborState.get(FACING)
                    && getFacing(neighborState) == direction.getOpposite()) {
                return state.with(CHEST_TYPE, chestType.getOpposite());
            }
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (state.isOf(newState.getBlock())) return;
        ChestType type = state.get(CHEST_TYPE);
        if (type == ChestType.SINGLE) return;

        Direction facing = state.get(FACING);
        BlockPos otherPos =  pos.offset(type == ChestType.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise());
        world.breakBlock(otherPos, false);
    }

    public static Direction getFacing(BlockState state) {
        Direction direction = state.get(FACING);
        return state.get(CHEST_TYPE) == ChestType.LEFT ? direction.rotateYClockwise() : direction.rotateYCounterclockwise();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(CHEST_TYPE)) {
            case LEFT:
                switch (state.get(FACING)) {
                    case NORTH:
                        return SHAPE_LEFT_NORTH;
                    case EAST:
                        return SHAPE_LEFT_EAST;
                    case SOUTH:
                        return SHAPE_LEFT_SOUTH;
                    case WEST:
                        return SHAPE_LEFT_WEST;
                }
            case RIGHT:
                switch (state.get(FACING)) {
                    case NORTH:
                        return SHAPE_RIGHT_NORTH;
                    case EAST:
                        return SHAPE_RIGHT_EAST;
                    case SOUTH:
                        return SHAPE_RIGHT_SOUTH;
                    case WEST:
                        return SHAPE_RIGHT_WEST;
                }
            default:
                return SHAPE_SINGLE;
        }
    }

    // region Settings
    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(FACING, CHEST_TYPE, WATERLOGGED); }
    // endregion
}
