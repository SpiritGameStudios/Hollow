package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import org.jspecify.annotations.Nullable;

public abstract class VerticalDoubleBlock extends Block {
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public VerticalDoubleBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(HALF, DoubleBlockHalf.LOWER)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Level level = ctx.getLevel();
		BlockPos above = ctx.getClickedPos().above();
		BlockState aboveState = level.getBlockState(above);

		return level.isInWorldBounds(above) && aboveState.canBeReplaced(ctx) ? this.defaultBlockState() : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
		level.setBlockAndUpdate(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER));
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockState lower = level.getBlockState(pos.below());
			return lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER;
		}

		return super.canSurvive(state, level, pos);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide()) {
			if (player.isCreative()) {
				this.onBreakInCreative(level, pos, state, player);
			}
			else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
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

	protected void onBreakInCreative(Level level, BlockPos pos, BlockState state, Player player) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockPos lowerPos = pos.below();
			BlockState lower = level.getBlockState(lowerPos);

			if (lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER) {
				level.removeBlock(lowerPos, false);
				level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, lowerPos, getId(lower));
			}
		}
	}
}
