/*
package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.state.properties.TripleBlockThird;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public class NewCattailBlock extends DoublePlantBlock {
	public static final MapCodec<DoublePlantBlock> CODEC = simpleCodec(DoublePlantBlock::new);
	public static final EnumProperty<TripleBlockThird> THIRD = EnumProperty.create("third", TripleBlockThird.class);

	@Override
	public MapCodec<? extends DoublePlantBlock> codec() {
		return CODEC;
	}

	public NewCattailBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(THIRD, TripleBlockThird.LOWER));
	}

	@Override
	protected BlockState updateShape(
		BlockState state,
		LevelReader level,
		ScheduledTickAccess ticks,
		BlockPos pos,
		Direction directionToNeighbour,
		BlockPos neighbourPos,
		BlockState neighbourState,
		RandomSource random
	) {
		TripleBlockThird third = state.getValue(THIRD);
		if (directionToNeighbour.getAxis() == Direction.Axis.Y && third == DoubleBlockHalf.LOWER == (directionToNeighbour == Direction.UP) && (!neighbourState.is(this) || neighbourState.getValue(THIRD) == third)) {
			return Blocks.AIR.defaultBlockState();
		}
		if (third == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN) {
			state.canSurvive(level, pos);
		}
		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);

	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		return pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
		BlockPos abovePos = pos.above();
		level.setBlockAndUpdate(abovePos, copyWaterloggedFrom(level, abovePos, this.defaultBlockState().setValue(THIRD, DoubleBlockHalf.UPPER)));
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(THIRD) != DoubleBlockHalf.UPPER) {
			return super.canSurvive(state, level, pos);
		}

		BlockState belowState = level.getBlockState(pos.below());
		return belowState.is(this) && belowState.getValue(THIRD) == DoubleBlockHalf.LOWER;
	}

	public static void placeAt(LevelAccessor level, BlockState state, BlockPos lowerPos, @Block.UpdateFlags int updateType) {
		BlockPos upperPos = lowerPos.above();
		level.setBlock(lowerPos, copyWaterloggedFrom(level, lowerPos, state.setValue(THIRD, DoubleBlockHalf.LOWER)), updateType);
		level.setBlock(upperPos, copyWaterloggedFrom(level, upperPos, state.setValue(THIRD, DoubleBlockHalf.UPPER)), updateType);
	}

	public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
		return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos)) : state;
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide()) {
			if (player.preventsBlockDrops()) {
				preventDropFromBottomPart(level, pos, state, player);
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public void playerDestroy(
		Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith
	) {
		super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith);
	}

	protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		DoubleBlockHalf part = state.getValue(THIRD);
		if (part == DoubleBlockHalf.UPPER) {
			BlockPos bottomPos = pos.below();
			BlockState bottomState = level.getBlockState(bottomPos);
			if (bottomState.is(state.getBlock()) && bottomState.getValue(THIRD) == DoubleBlockHalf.LOWER) {
				BlockState blockState = bottomState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				level.setBlock(bottomPos, blockState, 35);
				level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(THIRD);
	}

	@Override
	protected long getSeed(BlockState state, BlockPos pos) {
		return Mth.getSeed(pos.getX(), pos.below(state.getValue(THIRD) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}
}
*/
