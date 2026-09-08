package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.tags.HollowEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class HollowLogBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape HOLE_SHAPE = cube(12.0, 12.0, 16.0);
    public static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateAllAxis(
		Shapes.join(Shapes.block(), HOLE_SHAPE, BooleanOp.ONLY_FIRST)
    );

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public final Block log;
    public final boolean isStripped;

    public HollowLogBlock(Properties settings, Block log, boolean isStripped) {
        super(settings);
        this.log = log;
        this.isStripped = isStripped;
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(AXIS, Direction.Axis.Y)
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return !state.getValue(WATERLOGGED) && state.getValue(AXIS) == Direction.Axis.Y;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState()
                .setValue(AXIS, ctx.getClickedFace().getAxis())
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(AXIS));
    }

	public static boolean isClimbableHollowLog(BlockState state, Entity entity) {
		if (!entity.is(HollowEntityTypeTags.CAN_CLIMB_HOLLOW_LOGS) || !isVerticalLog(state)) {
			return false;
		}

		Level level = entity.level();
		BlockPos blockPos = entity.blockPosition();

		return isVerticalLog(level, blockPos.above()) || isVerticalLog(level, blockPos.below());
	}

	public static boolean isVerticalLog(Level level, BlockPos pos) {
		return isVerticalLog(level.getBlockState(pos));
	}

	public static boolean isVerticalLog(BlockState state) {
		return state.getBlock() instanceof HollowLogBlock && state.getValue(AXIS) == Direction.Axis.Y;
	}
}
