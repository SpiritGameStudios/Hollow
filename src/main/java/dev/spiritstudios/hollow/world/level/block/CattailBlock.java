package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.state.properties.TripleBlockThird;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
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
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos lowerPos = context.getClickedPos();

		return isValidPlacementPosition(level, lowerPos) ? this.defaultBlockState().setValue(WATERLOGGED, true) : null;
	}

	public static boolean isValidPlacementPosition(Level level, BlockPos lowerPos) {
		BlockPos middlePos = lowerPos.above();
		BlockPos upperPos = lowerPos.above(2);

		if (!level.isInWorldBounds(upperPos) || !level.isWaterAt(lowerPos))
			return false;

		BlockState middleState = level.getBlockState(middlePos);
		BlockState upperState = level.getBlockState(upperPos);

		return middleState.canBeReplaced() && upperState.canBeReplaced();
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(THIRD) == TripleBlockThird.LOWER ?
			super.canSurvive(state, level, pos) :
			level.getBlockState(pos.below()).is(this);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
		placeAt(level, state, pos, UPDATE_ALL, false);
	}

	public static BlockState withThird(BlockState state, TripleBlockThird third) {
		return state.trySetValue(THIRD, third);
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
		return state.trySetValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos));
	}

	public static BlockState copyWaterloggedWithThird(LevelReader level, BlockPos pos, BlockState state, TripleBlockThird third) {
		return copyWaterloggedFrom(level, pos, withThird(state, third));
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
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
		builder.add(THIRD, WATERLOGGED);
	}

	@Override
	protected long getSeed(BlockState state, BlockPos pos) {
		return super.getSeed(state, state.getValue(THIRD).getLowerPos(pos));
	}
}
