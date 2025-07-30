package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Map;

public class PolyporeBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty POLYPORE_AMOUNT = IntProperty.of("amount", 1, 3);

    public static final Map<Direction, VoxelShape> SHAPES_BY_DIRECTION = VoxelShapes.createHorizontalFacingShapeMap(createCuboidShape(1, 1, 8, 15, 15, 16));
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
    protected boolean isTransparent(BlockState state) {
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
        return SHAPES_BY_DIRECTION.get(state.get(Properties.HORIZONTAL_FACING));
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
