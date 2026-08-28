package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.state.properties.TripleBlockThird;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public class CattailBlock extends VegetationBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<DoublePlantBlock> CODEC = simpleCodec(DoublePlantBlock::new);

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<TripleBlockThird> THIRD = EnumProperty.create("third", TripleBlockThird.class);

	public CattailBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.defaultBlockState()
				.setValue(THIRD, TripleBlockThird.LOWER)
				.setValue(WATERLOGGED, false)
		);
	}

	@Override
	public MapCodec<? extends DoublePlantBlock> codec() {
		return CODEC;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		TripleBlockThird third = state.getValue(THIRD);

		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		boolean isLower = third == TripleBlockThird.LOWER;
		boolean vertical = directionToNeighbour.getAxis().isVertical();

		if (vertical && (isLower == (directionToNeighbour == Direction.UP) || third == TripleBlockThird.MIDDLE)) {
			if (!neighbourState.is(this) || neighbourState.getValue(THIRD) == third) {
				return Blocks.AIR.defaultBlockState();
			}
		} else if (isLower && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();

		BlockPos pos = context.getClickedPos();
		BlockPos middlePos = pos.above();
		BlockPos upperPos = pos.above(2);

		BlockState middleState = level.getBlockState(middlePos);
		BlockState upperState = level.getBlockState(upperPos);

		return level.isInWorldBounds(upperPos) &&
			!level.isWaterAt(upperPos) &&
			upperState.canBeReplaced(context) &&
			middleState.canBeReplaced(context) ?
			this.defaultBlockState().setValue(WATERLOGGED, true) :
			null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
		level.setBlockAndUpdate(
			pos.above(),
			copyWaterloggedFrom(level, pos.above(), state.setValue(THIRD, TripleBlockThird.MIDDLE))
		);

		level.setBlockAndUpdate(
			pos.above(2),
			copyWaterloggedFrom(level, pos.above(2), state.setValue(THIRD, TripleBlockThird.UPPER))
		);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return switch (state.getValue(THIRD)) {
			case UPPER -> {
				BlockState middleState = level.getBlockState(pos.below());
				BlockState lowerState = level.getBlockState(pos.below(2));

				yield middleState.is(this) &&
					lowerState.is(this) &&
					!level.isWaterAt(pos);
			}
			case MIDDLE -> {
				BlockState lowerState = level.getBlockState(pos.below());

				yield lowerState.is(this);
			}
			case LOWER -> super.canSurvive(state, level, pos) && level.isWaterAt(pos);
		};
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
		return state.hasProperty(BlockStateProperties.WATERLOGGED) ?
			state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos)) :
			state;
	}

	public static boolean placeAt(LevelAccessor level, BlockState state, BlockPos pos, @Block.UpdateFlags int updateFlags) {
		BlockPos middlePos = pos.above();
		BlockPos upperPos = pos.above(2);

		boolean result = true;

		result &= level.setBlock(
			pos,
			copyWaterloggedFrom(level, pos, state.setValue(THIRD, TripleBlockThird.LOWER)),
			updateFlags
		);

		result &= level.setBlock(
			middlePos,
			copyWaterloggedFrom(level, middlePos, state.setValue(THIRD, TripleBlockThird.MIDDLE)),
			updateFlags
		);

		result &= level.setBlock(
			upperPos,
			copyWaterloggedFrom(level, upperPos, state.setValue(THIRD, TripleBlockThird.UPPER)),
			updateFlags
		);

		return result;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide()) {
			TripleBlockThird third = state.getValue(THIRD);
			if (player.preventsBlockDrops()) {
				preventDropsFromOtherParts(level, pos, state, player, third);
			} else {
				// dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}

	protected static void preventDropsFromOtherParts(Level level, BlockPos pos, BlockState state, Player player, TripleBlockThird third) {
		third.doForBothRelativePositions(pos, blockPos -> {
			BlockState blockState = level.getBlockState(blockPos);
			if (blockState.is(state.getBlock())) {
				level.setBlock(blockPos, level.getFluidState(blockPos).createLegacyBlock(), UPDATE_SUPPRESS_DROPS | UPDATE_ALL);
				level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
			}
		});
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder
			.add(THIRD)
			.add(WATERLOGGED);
	}

	@Override
	protected long getSeed(BlockState state, BlockPos pos) {
		TripleBlockThird third = state.getValue(THIRD);
		return super.getSeed(state, third == TripleBlockThird.LOWER ? pos : third.getRelativePos1(pos));
	}
}
