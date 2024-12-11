package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.math.VoxelShapeHelper;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class PolyporeBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty POLYPORE_AMOUNT = IntProperty.of("amount", 1, 3);

    public static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(1, 1, 8, 15, 15, 16);
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeHelper.rotateHorizontal(Direction.SOUTH, Direction.NORTH, SHAPE_NORTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeHelper.rotateHorizontal(Direction.EAST, Direction.NORTH, SHAPE_NORTH);
    public static final VoxelShape SHAPE_WEST = VoxelShapeHelper.rotateHorizontal(Direction.WEST, Direction.NORTH, SHAPE_NORTH);

    public static final MapCodec<PolyporeBlock> CODEC = createCodec(PolyporeBlock::new);

    public PolyporeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(POLYPORE_AMOUNT, 1));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, POLYPORE_AMOUNT);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return !context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(POLYPORE_AMOUNT) < 3 || super.canReplace(state, context);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) return blockState.cycle(POLYPORE_AMOUNT);

        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                BlockState blockState2 = getDefaultState().with(Properties.HORIZONTAL_FACING, direction.getOpposite());
                if (blockState2.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) return blockState2;
            }
        }

        return null;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(POLYPORE_AMOUNT) < 3)
            world.setBlockState(pos, state.cycle(POLYPORE_AMOUNT), Block.NOTIFY_LISTENERS);
        else dropStack(world, pos, new ItemStack(this));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> SHAPE_NORTH;
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> super.getOutlineShape(state, world, pos, context);
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(Properties.HORIZONTAL_FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);

        return blockState.isSideSolidFullSquare(world, blockPos, direction) &&
                blockState.isIn(HollowBlocks.Tags.POLYPORE_PLACEABLE_ON);
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }
}
