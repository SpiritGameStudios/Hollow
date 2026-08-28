package dev.spiritstudios.hollow.world.level.block.jar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class BaseJarBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final VoxelShape BODY_SHAPE = column(8.0, 0.0, 10.0);
	public static final VoxelShape CORK_SHAPE = column(6.0, 10.0, 12.0);

	public static final float HANGING_SHAPE_Y_DIFF = 0.0625F;

    public static final VoxelShape SHAPE = Shapes.or(BODY_SHAPE, CORK_SHAPE);
	private static final VoxelShape SHAPE_HANGING = SHAPE.move(0.0, HANGING_SHAPE_Y_DIFF, 0.0).optimize();

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

	public BaseJarBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(HANGING, false)
			.setValue(WATERLOGGED, false)
		);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return state.canSurvive(level, pos) ? super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random) : Blocks.AIR.defaultBlockState();
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return !state.getValue(HANGING) || canSupportCenter(level, pos.relative(Direction.UP), Direction.DOWN);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		return this.defaultBlockState()
			.setValue(WATERLOGGED, level.getFluidState(pos).is(Fluids.WATER))
			.setValue(HANGING, context.getClickedFace() == Direction.DOWN);
	}

	@Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? SHAPE_HANGING : SHAPE;
    }

	@Override
	protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
		if (level instanceof ServerLevel serverLevel) {
			BlockPos pos = blockHit.getBlockPos();

			if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel))
				level.destroyBlock(pos, false, projectile);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, HANGING);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
