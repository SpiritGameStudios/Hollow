package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StoneChestLidBlock extends Block {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ChestType> CHEST_TYPE = BlockStateProperties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // region VoxelShapes
    public static final VoxelShape SHAPE_SINGLE = Block.box(1, 0, 1, 15, 4, 15);


    private static final Map<Direction, VoxelShape> DOUBLE_LEFT_SHAPES_BY_DIRECTION = Shapes.rotateHorizontal(box(1, 0, 1, 16, 4, 15));

    private static final Map<Direction, VoxelShape> DOUBLE_RIGHT_SHAPES_BY_DIRECTION = Shapes.rotateHorizontal(box(0, 0, 1, 15, 4, 15));
    // endregion
    
    public StoneChestLidBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CHEST_TYPE, ChestType.SINGLE)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        ChestType chestType = ChestType.SINGLE;
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean shouldCancel = ctx.isSecondaryUseActive();
        Direction side = ctx.getClickedFace();

        if (side.getAxis().isHorizontal() && shouldCancel) {
            Direction neighborDirection = this.getNeighborChestDirection(ctx, side.getOpposite());
            if (neighborDirection != null && neighborDirection.getAxis() != side.getAxis()) {
                direction = neighborDirection;
                chestType = neighborDirection.getCounterClockWise() == side.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chestType == ChestType.SINGLE && !shouldCancel)
            if (direction == this.getNeighborChestDirection(ctx, direction.getClockWise()))
                chestType = ChestType.LEFT;
            else if (direction == this.getNeighborChestDirection(ctx, direction.getCounterClockWise()))
                chestType = ChestType.RIGHT;

        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(CHEST_TYPE, chestType)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Nullable
    private Direction getNeighborChestDirection(BlockPlaceContext ctx, Direction dir) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(dir));
        return blockState.is(this) && blockState.getValue(CHEST_TYPE) == ChestType.SINGLE ? blockState.getValue(FACING) : null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation(state.getValue(FACING))); }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

        if (!neighborState.is(this) || !direction.getAxis().isHorizontal()) {
            if (getFacing(state) == direction) return state.setValue(CHEST_TYPE, ChestType.SINGLE);
        } else {
            ChestType chestType = neighborState.getValue(CHEST_TYPE);
            if (state.getValue(CHEST_TYPE) == ChestType.SINGLE
                    && chestType != ChestType.SINGLE
                    && state.getValue(FACING) == neighborState.getValue(FACING)
                    && getFacing(neighborState) == direction.getOpposite()) {
                return state.setValue(CHEST_TYPE, chestType.getOpposite());
            }
        }

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
        ChestType type = state.getValue(CHEST_TYPE);
        if (type == ChestType.SINGLE) return;

        Direction facing = state.getValue(FACING);
        BlockPos otherPos =  pos.relative(type == ChestType.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
        world.destroyBlock(otherPos, false);

    }

    public static Direction getFacing(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(CHEST_TYPE) == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CHEST_TYPE)) {
            case LEFT -> DOUBLE_LEFT_SHAPES_BY_DIRECTION.get(state.getValue(FACING));
            case RIGHT -> DOUBLE_RIGHT_SHAPES_BY_DIRECTION.get(state.getValue(FACING));
            default -> SHAPE_SINGLE;
        };
    }

    // region Settings
    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING, CHEST_TYPE, WATERLOGGED); }
    // endregion
}
