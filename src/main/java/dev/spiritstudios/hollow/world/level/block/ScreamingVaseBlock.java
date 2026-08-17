package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.mojang.math.OctahedralGroup;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScreamingVaseBlock extends BaseEntityBlock {
	public static final MapCodec<ScreamingVaseBlock> CODEC = simpleCodec(ScreamingVaseBlock::new);

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public ScreamingVaseBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER));
	}

	public static final VoxelShape LOWER_SHAPE =
			Block.box(2, 0, 2, 14, 16, 14);

	public static final VoxelShape UPPER_SHAPE_NS = Shapes.or(
			Block.box(2, 0, 2, 14, 13, 14),
			Block.box(5, 13, 5, 11, 16, 11),
			Block.box(0, 7, 7, 16, 10, 9)
	);

	public static final VoxelShape UPPER_SHAPE_EW = Shapes.rotate(UPPER_SHAPE_NS, OctahedralGroup.ROT_90_Y_POS);


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ?
				state.getValue(FACING).getAxis() == Direction.Axis.X ? UPPER_SHAPE_EW : UPPER_SHAPE_NS :
				LOWER_SHAPE;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		EchoingVaseBlockEntity blockEntity = (EchoingVaseBlockEntity) level.getBlockEntity(pos);
		if (blockEntity == null) {
			Hollow.LOGGER.error("Failed to find BE at {}", state);
			return;
		}
		Objects.requireNonNull(blockEntity).onEntityCollision(state, level, pos, entity);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EchoingVaseBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide()) return InteractionResult.SUCCESS;

		if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
			pos = pos.below();

		EchoingVaseBlockEntity blockEntity = (EchoingVaseBlockEntity) world.getBlockEntity(pos);
		Objects.requireNonNull(blockEntity).use(player, hand);
		return InteractionResult.CONSUME;
	}

	@Override
	protected boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
		super.triggerEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(type, data);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, HollowBlockEntityTypes.ECHOING_VASE, EchoingVaseBlockEntity::tick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);

		builder.add(FACING);
		builder.add(HALF);
	}


	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Level world = ctx.getLevel();

		return pos.getY() < world.getMaxY() - 1 && world.getBlockState(pos.above()).canBeReplaced(ctx) ?
				defaultBlockState()
						.setValue(FACING, ctx.getHorizontalDirection().getOpposite()) : null;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		world.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
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

		if (state.getValue(HALF) == DoubleBlockHalf.LOWER)
			onBreakLower(world, pos, state, player);
		else
			onBreakLower(world, pos.below(), world.getBlockState(pos.below()), player);

		if (player.isCreative()) onBreakInCreative(world, pos, state, player);
		else dropResources(state, world, pos, null, player, player.getMainHandItem());

		return super.playerWillDestroy(world, pos, state, player);
	}

	public static void onBreakLower(Level world, BlockPos pos, BlockState state, @Nullable Entity causer) {
		if (world.isClientSide()) return;

		((ServerLevel) world).sendParticles(
				HollowParticleTypes.SCREAM,
				pos.getX(), pos.getY(), pos.getZ(),
				1, 0, 0, 0, 0
		);

		world.playSound(null, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS);
		world.gameEvent(GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos, GameEvent.Context.of(causer));
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

		BlockPos lowerPos = pos.relative(state.getValue(FACING).getClockWise());
		BlockState lower = world.getBlockState(lowerPos);

		if (!lower.is(state.getBlock()) || lower.getValue(HALF) != DoubleBlockHalf.LOWER)
			return;

		BlockState lowerFluid = lower.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
		world.setBlock(lowerPos, lowerFluid, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
		world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, lowerPos, Block.getId(lower));
	}

	@Override
	protected MapCodec<ScreamingVaseBlock> codec() {
		return CODEC;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}