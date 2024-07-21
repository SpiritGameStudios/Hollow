package dev.callmeecho.hollow.main.block;

import com.mojang.serialization.MapCodec;
import dev.callmeecho.cabinetapi.util.VoxelShapeHelper;
import dev.callmeecho.hollow.main.block.entity.EchoingPotBlockEntity;
import dev.callmeecho.hollow.main.block.entity.JarBlockEntity;
import dev.callmeecho.hollow.main.block.entity.StoneChestBlockEntity;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StoneChestBlock extends BlockWithEntity implements Waterloggable {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<ChestType> CHEST_TYPE = Properties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public static final MapCodec<StoneChestBlock> CODEC = createCodec(StoneChestBlock::new);
    
    public static final VoxelShape SHAPE_SINGLE = VoxelShapes.union(
            createCuboidShape(1, 1, 1, 15, 16, 15),
            createCuboidShape(0, 0, 0, 16, 1, 16)
    );
    
    public static final VoxelShape SHAPE_LEFT_NORTH = VoxelShapes.union(
            createCuboidShape(1, 1, 1, 16, 16, 15),
            createCuboidShape(0, 0, 0, 16, 1, 16)
    );
    public static final VoxelShape SHAPE_LEFT_EAST = VoxelShapeHelper.rotate(Direction.EAST, Direction.NORTH, SHAPE_LEFT_NORTH);
    public static final VoxelShape SHAPE_LEFT_SOUTH = VoxelShapeHelper.rotate(Direction.SOUTH, Direction.NORTH, SHAPE_LEFT_NORTH);
    public static final VoxelShape SHAPE_LEFT_WEST = VoxelShapeHelper.rotate(Direction.WEST, Direction.NORTH, SHAPE_LEFT_NORTH);
    
    public static final VoxelShape SHAPE_RIGHT_NORTH = VoxelShapes.union(
            createCuboidShape(0, 1, 1, 15, 16, 15),
            createCuboidShape(0, 0, 0, 16, 1, 16)
    );
    public static final VoxelShape SHAPE_RIGHT_EAST = VoxelShapeHelper.rotate(Direction.EAST, Direction.NORTH, SHAPE_RIGHT_NORTH);
    public static final VoxelShape SHAPE_RIGHT_SOUTH = VoxelShapeHelper.rotate(Direction.SOUTH, Direction.NORTH, SHAPE_RIGHT_NORTH);
    public static final VoxelShape SHAPE_RIGHT_WEST = VoxelShapeHelper.rotate(Direction.WEST, Direction.NORTH, SHAPE_RIGHT_NORTH);
    
    public StoneChestBlock(Settings settings) {
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
        boolean bl = ctx.shouldCancelInteraction();
        Direction direction2 = ctx.getSide();
        if (direction2.getAxis().isHorizontal() && bl) {
            Direction direction3 = this.getNeighborChestDirection(ctx, direction2.getOpposite());
            if (direction3 != null && direction3.getAxis() != direction2.getAxis()) {
                direction = direction3;
                chestType = direction3.rotateYCounterclockwise() == direction2.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chestType == ChestType.SINGLE && !bl) {
            if (direction == this.getNeighborChestDirection(ctx, direction.rotateYClockwise())) {
                chestType = ChestType.LEFT;
            } else if (direction == this.getNeighborChestDirection(ctx, direction.rotateYCounterclockwise())) {
                chestType = ChestType.RIGHT;
            }
        }
        
        return this.getDefaultState().with(FACING, direction).with(CHEST_TYPE, chestType).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Nullable
    private Direction getNeighborChestDirection(ItemPlacementContext ctx, Direction dir) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(dir));
        return blockState.isOf(this) && blockState.get(CHEST_TYPE) == ChestType.SINGLE ? blockState.get(FACING) : null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoneChestBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CHEST_TYPE, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) { return state.with(FACING, rotation.rotate(state.get(FACING))); }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) { return state.rotate(mirror.getRotation(state.get(FACING))); }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        
        if (direction == Direction.UP && neighborState.isAir()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoneChestBlockEntity stoneChest) {
                stoneChest.aboveBroken();
            }
        }

        if (neighborState.isOf(this) && direction.getAxis().isHorizontal()) {
            ChestType chestType = neighborState.get(CHEST_TYPE);
            if (state.get(CHEST_TYPE) == ChestType.SINGLE
                    && chestType != ChestType.SINGLE
                    && state.get(FACING) == neighborState.get(FACING)
                    && getFacing(neighborState) == direction.getOpposite()) {
                return state.with(CHEST_TYPE, chestType.getOpposite());
            }
        } else if (getFacing(state) == direction) {
            return state.with(CHEST_TYPE, ChestType.SINGLE);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public static Direction getFacing(BlockState state) {
        Direction direction = state.get(FACING);
        return state.get(CHEST_TYPE) == ChestType.LEFT ? direction.rotateYClockwise() : direction.rotateYCounterclockwise();
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ItemActionResult.SUCCESS;

        StoneChestBlockEntity blockEntity = (StoneChestBlockEntity)world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).use(player, hand, hit.getSide());
        return ItemActionResult.CONSUME;
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

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoneChestBlockEntity) {
                ItemScatterer.spawn(world, pos, (StoneChestBlockEntity)blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }
}
