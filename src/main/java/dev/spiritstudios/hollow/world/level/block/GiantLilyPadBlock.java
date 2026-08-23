package dev.spiritstudios.hollow.world.level.block;

import com.mojang.math.OctahedralGroup;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.UnaryOperator;

public class GiantLilyPadBlock extends LilyPadBlock {
	private static final VoxelShape NORTH_WEST_SHAPE = box(2.0, 0.0, 2.0, 16.0, 1.5, 16.0);
	private static final VoxelShape NORTH_EAST_SHAPE = Shapes.rotate(NORTH_WEST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);
	private static final VoxelShape SOUTH_EAST_SHAPE = Shapes.rotate(NORTH_EAST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);
	private static final VoxelShape SOUTH_WEST_SHAPE = Shapes.rotate(SOUTH_EAST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);

	public static final EnumProperty<Piece> PIECE = EnumProperty.create("piece", Piece.class);
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

	public GiantLilyPadBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState()
			.setValue(PIECE, Piece.NORTH_WEST)
			.setValue(FACING, Direction.NORTH)
		);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());

		return this.canSurvive(state, level, pos) &&
			this.canSurvive(state, level, pos.east()) &&
			this.canSurvive(state, level, pos.south()) &&
			this.canSurvive(state, level, pos.east().south()) ? state : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (state.getValue(PIECE) == Piece.NORTH_WEST) {
			placePadBlocks(level, pos, state, Piece.NORTH_WEST, false);
		}
	}

	public static void placePadBlocks(Level level, BlockPos pos, BlockState state, Piece piece, boolean includeNorthWest) {
		BlockPos northWest = piece.getNorthWest(pos);
		if (includeNorthWest) level.setBlock(northWest, state, UPDATE_ALL_IMMEDIATE);

		level.setBlock(northWest.east(), state.setValue(PIECE, Piece.NORTH_EAST), UPDATE_ALL_IMMEDIATE);
		level.setBlock(northWest.south(), state.setValue(PIECE, Piece.SOUTH_WEST), UPDATE_ALL_IMMEDIATE);
		level.setBlock(northWest.south().east(), state.setValue(PIECE, Piece.SOUTH_EAST), UPDATE_ALL_IMMEDIATE);
	}

	public static boolean tryForm(BlockPlaceContext context, BlockState placementState) {
		if (!placementState.is(HollowBlockItemTags.FORMS_GIANT_LILY_PAD.block()) || context.isSecondaryUseActive())
			return false;

		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		BlockState giant = HollowBlocks.GIANT_LILY_PAD.defaultBlockState().setValue(FACING, context.getHorizontalDirection());

		for (Piece piece : Piece.values()) {
			label1:
			for (int i = 0; i < 4; i++) {
				for (BlockPos blockPos : piece.getAllPadPositions(clickedPos)) {
					if (!blockPos.equals(clickedPos) && !level.getBlockState(blockPos).is(HollowBlockItemTags.FORMS_GIANT_LILY_PAD.block())) {
						continue label1;
					}
				}

				placePadBlocks(level, clickedPos, giant, piece, true);

				return true;
			}
		}

		return false;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
		super.affectNeighborsAfterRemoval(state, world, pos, moved);

		for (BlockPos blockPos : state.getValue(PIECE).getAllPadPositions(pos)) {
			if (!blockPos.equals(pos) && world.getBlockState(blockPos).is(this)) {
				world.destroyBlock(blockPos, false);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PIECE, FACING);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(PIECE)) {
			case NORTH_WEST -> NORTH_WEST_SHAPE;
			case NORTH_EAST -> NORTH_EAST_SHAPE;
			case SOUTH_WEST -> SOUTH_WEST_SHAPE;
			case SOUTH_EAST -> SOUTH_EAST_SHAPE;
		};
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return Items.LILY_PAD.getDefaultInstance();
	}

	public enum Piece implements StringRepresentable {
		NORTH_WEST(pos -> pos),
		NORTH_EAST(pos -> pos.west()),
		SOUTH_WEST(pos -> pos.north()),
		SOUTH_EAST(pos -> pos.north().west());

		private final UnaryOperator<BlockPos> northWestOffset;

		Piece(UnaryOperator<BlockPos> northWestOffset) {
			this.northWestOffset = northWestOffset;
		}

		public BlockPos getNorthWest(BlockPos pos) {
			return this.northWestOffset.apply(pos);
		}

		public BlockPos[] getAllPadPositions(BlockPos pos) {
			BlockPos northWest = this.getNorthWest(pos);
			return new BlockPos[] {
				northWest,
				northWest.east(),
				northWest.south(),
				northWest.south().east()
			};
		}

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
