package dev.spiritstudios.hollow.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public abstract class VerticalDoubleBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    
    public VerticalDoubleBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        return pos.getY() < world.getTopY() - 1 && world.getBlockState(pos.up()).canReplace(ctx) ?
                getDefaultState()
                        .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite()): null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            BlockState lower = world.getBlockState(pos.down());
            return lower.isOf(this) && lower.get(HALF) == DoubleBlockHalf.LOWER;
        }

        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient()) return super.onBreak(world, pos, state, player);

        if (player.isCreative()) onBreakInCreative(world, pos, state, player);
        else dropStacks(state, world, pos, null, player, player.getMainHandStack());

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, tool);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.get(HALF);

        if (direction.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP)) {
            return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)
                    ? Blocks.AIR.getDefaultState()
                    : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
 
        return neighborState.isOf(this) && neighborState.get(HALF) != half
                ? neighborState.with(HALF, half)
                : Blocks.AIR.getDefaultState();
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        if (half == DoubleBlockHalf.LOWER) return;

        BlockPos lowerPos = pos.down();
        BlockState lower = world.getBlockState(lowerPos);

        if (!lower.isOf(state.getBlock()) || lower.get(HALF) != DoubleBlockHalf.LOWER)
            return;

        BlockState lowerFluid = lower.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        world.setBlockState(lowerPos, lowerFluid, Block.NOTIFY_ALL | Block.SKIP_DROPS);
        world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, lowerPos, Block.getRawIdFromState(lower));
    }
}
