package dev.spiritstudios.hollow.world.level.block.pot;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import dev.spiritstudios.hollow.world.level.block.VerticalDoubleBlock;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.entity.pot.FallingPotBlockEntity;
import dev.spiritstudios.hollow.world.level.block.entity.pot.PotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class ScreamingVaseBlock extends VerticalDoubleBlock implements TickingEntityBlock<FallingPotBlockEntity> {
	public static final MapCodec<ScreamingVaseBlock> CODEC = simpleCodec(ScreamingVaseBlock::new);

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public ScreamingVaseBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER)
		);
	}

	public static final VoxelShape LOWER_SHAPE = column(12, 0, 16);
	public static final VoxelShape UPPER_SHAPE = Shapes.or(column(12, 0, 13), column(6, 13, 16));

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (level.getBlockEntity(pos) instanceof FallingPotBlockEntity blockEntity) {
			blockEntity.onEntityCollision(state, level, pos, entity);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FallingPotBlockEntity(pos, state);
	}

	@Override
	public BlockEntityType<FallingPotBlockEntity> getType() {
		return HollowBlockEntityTypes.FALLING_POT;
	}

	@Override
	protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
		super.triggerEvent(state, level, pos, b0, b1);
		BlockEntity blockEntity = level.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(b0, b1);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide()) return InteractionResult.SUCCESS;

		if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
			pos = pos.below();

		if (world.getBlockEntity(pos) instanceof PotBlockEntity blockEntity) {
			blockEntity.use(player, hand);
		}

		return InteractionResult.CONSUME;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return this.validateTicker(type, FallingPotBlockEntity::tick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(FACING));
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState superState = super.getStateForPlacement(ctx);
		return superState == null ? null : superState.setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		onBreakLower(level, state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below(), player);
		return super.playerWillDestroy(level, pos, state, player);
	}

	public static void onBreakLower(Level level, BlockPos pos, @Nullable Entity causer) {
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(
				HollowParticleTypes.SCREAM,
				pos.getX(), pos.getY(), pos.getZ(),
				1, 0, 0, 0, 0
			);

			level.playSound(null, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS);
			level.gameEvent(GameEvent.SHRIEK, pos, GameEvent.Context.of(causer));
		}
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
