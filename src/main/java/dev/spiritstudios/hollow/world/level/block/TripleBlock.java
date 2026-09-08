package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.world.level.block.state.properties.TripleBlockThird;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public abstract class TripleBlock extends Block {
	public static final EnumProperty<TripleBlockThird> THIRD = EnumProperty.create("third", TripleBlockThird.class);

	public TripleBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(THIRD, TripleBlockThird.LOWER)
		);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (!state.canSurvive(level, pos))
			return Blocks.AIR.defaultBlockState();

		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos lowerPos = context.getClickedPos();

		return this.isValidPlacementPosition(level, lowerPos) ? super.getStateForPlacement(context) : null;
	}

	public boolean isValidPlacementPosition(Level level, BlockPos lowerPos) {
		BlockPos upperPos = lowerPos.above(2);
		BlockState middleState = level.getBlockState(lowerPos.above());
		BlockState upperState = level.getBlockState(upperPos);

		return level.isInWorldBounds(upperPos) && middleState.canBeReplaced() && upperState.canBeReplaced();
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(THIRD) == TripleBlockThird.LOWER ?
			this.canLowerBlockSurvive(state, level, pos) :
			level.getBlockState(pos.below()).is(this);
	}

	protected boolean canLowerBlockSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return true;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
		placeAt(level, state, pos, UPDATE_ALL, false);
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
		return state.trySetValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos));
	}

	public static BlockState copyWaterloggedWithThird(LevelReader level, BlockPos pos, BlockState state, TripleBlockThird third) {
		return copyWaterloggedFrom(level, pos, withThird(state, third));
	}

	public static BlockState withThird(BlockState state, TripleBlockThird third) {
		return state.trySetValue(THIRD, third);
	}

	public static boolean placeAt(LevelAccessor level, BlockState state, BlockPos lowerPos, @Block.UpdateFlags int updateFlags, boolean includeLower) {
		BlockPos middlePos = lowerPos.above();
		BlockPos upperPos = lowerPos.above(2);

		boolean result = true;

		if (includeLower) {
			result &= level.setBlock(lowerPos, copyWaterloggedWithThird(level, lowerPos, state, TripleBlockThird.LOWER), updateFlags);
		}

		result &= level.setBlock(middlePos, copyWaterloggedWithThird(level, middlePos, state, TripleBlockThird.MIDDLE), updateFlags);
		result &= level.setBlock(upperPos, copyWaterloggedWithThird(level, upperPos, state, TripleBlockThird.UPPER), updateFlags);

		return result;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
		super.affectNeighborsAfterRemoval(state, level, pos, moved);

		TripleBlockThird third = state.getValue(THIRD);

		level.destroyBlock(third.getRelativePos1(pos), false);
		level.destroyBlock(third.getRelativePos2(pos), false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(THIRD);
	}

	@Override
	protected long getSeed(BlockState state, BlockPos pos) {
		return super.getSeed(state, state.getValue(THIRD).getLowerPos(pos));
	}
}
