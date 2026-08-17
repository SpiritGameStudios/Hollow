package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

public abstract class VerticalDoubleBlock extends Block {
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public VerticalDoubleBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Level world = ctx.getLevel();

		return pos.getY() < world.getMaxY() - 1 && world.getBlockState(pos.above()).canBeReplaced(ctx) ?
				defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite()): null;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		world.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockState lower = world.getBlockState(pos.below());
			return lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER;
		}

		return super.canSurvive(state, world, pos);
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (world.isClientSide()) return super.playerWillDestroy(world, pos, state, player);

		if (player.isCreative()) onBreakInCreative(world, pos, state, player);
		else dropResources(state, world, pos, null, player, player.getMainHandItem());

		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, tool);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		DoubleBlockHalf half = state.getValue(HALF);

		if (direction.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP)) {
			return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(world, pos)
					? Blocks.AIR.defaultBlockState()
					: super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		}

		return neighborState.is(this) && neighborState.getValue(HALF) != half
				? neighborState.setValue(HALF, half)
				: Blocks.AIR.defaultBlockState();
	}

	protected static void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER) return;

		BlockPos lowerPos = pos.below();
		BlockState lower = world.getBlockState(lowerPos);

		if (!lower.is(state.getBlock()) || lower.getValue(HALF) != DoubleBlockHalf.LOWER)
			return;

		BlockState lowerFluid = lower.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
		world.setBlock(lowerPos, lowerFluid, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
		world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, lowerPos, Block.getId(lower));
	}
}