package dev.spiritstudios.hollow.world.level.block;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.EchoingVaseBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Pattern;

public class EchoingVaseBlock extends VerticalDoubleBlock implements EntityBlock {
	public static final MapCodec<EchoingVaseBlock> CODEC = simpleCodec(EchoingVaseBlock::new);

	public EchoingVaseBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	public static final VoxelShape LOWER_SHAPE =
			Block.box(2, 0, 2, 14, 16, 14);

	public static final VoxelShape UPPER_SHAPE = Shapes.or(
			Block.box(2, 0, 2, 14, 4, 14),
			Block.box(4, 4, 4, 12, 8, 12)
	);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EchoingVaseBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide()) return InteractionResult.SUCCESS;

		if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)
			pos = pos.below();

		EchoingVaseBlockEntity blockEntity = (EchoingVaseBlockEntity) world.getBlockEntity(pos);
		Objects.requireNonNull(blockEntity).use(player, hand);
		return InteractionResult.CONSUME;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
		super.triggerEvent(state, world, pos, type, data);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(type, data);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	protected MapCodec<? extends EchoingVaseBlock> codec() {
		return CODEC;
	}

	public static class ObaboBlock extends EchoingVaseBlock {
		public static final Pattern DIRE_CURSE = Pattern.compile("[oO0](?:[^0-9a-zA-Z]*|\\s)*[bB](?:[^0-9a-zA-Z]*|\\s)*[aA](?:[^0-9a-zA-Z]*|\\s)[bB](?:[^0-9a-zA-Z]*|\\s)*[oO0]");
		public static final MapCodec<ObaboBlock> CODEC = simpleCodec(ObaboBlock::new);
		public static Function4<BlockState, Level, BlockPos, RandomSource, Boolean> cb = null;

		public static void invokeCurse(ServerPlayer player) {
//			player.damage(player.getDamageSources().create(HollowDamageTypes.DIRE_CURSE), 100);
		}

		public ObaboBlock(Properties settings) {
			super(settings);
		}

		@Override
		protected RenderShape getRenderShape(BlockState state) {
			return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? RenderShape.INVISIBLE : super.getRenderShape(state);
		}

		@Override
		protected MapCodec<ObaboBlock> codec() {
			return CODEC;
		}

		@Override
		public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
			if (cb != null) cb.apply(state, world, pos, random);
		}
	}
}