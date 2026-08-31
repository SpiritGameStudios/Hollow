package dev.spiritstudios.hollow.world.level.block;

import com.mojang.math.OctahedralGroup;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import dev.spiritstudios.hollow.world.level.block.state.properties.LilyPadPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelWriter;
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

public class GiantLilyPadBlock extends LilyPadBlock {
	private static final VoxelShape NORTH_WEST_SHAPE = box(2.0, 0.0, 2.0, 16.0, 1.5, 16.0);
	private static final VoxelShape NORTH_EAST_SHAPE = Shapes.rotate(NORTH_WEST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);
	private static final VoxelShape SOUTH_EAST_SHAPE = Shapes.rotate(NORTH_EAST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);
	private static final VoxelShape SOUTH_WEST_SHAPE = Shapes.rotate(SOUTH_EAST_SHAPE, OctahedralGroup.BLOCK_ROT_Y_90);

	public static final EnumProperty<LilyPadPiece> PIECE = EnumProperty.create("piece", LilyPadPiece.class);
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

	public GiantLilyPadBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState()
			.setValue(PIECE, LilyPadPiece.NORTH_WEST)
			.setValue(FACING, Direction.NORTH)
		);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());

		return isValidPlacementPosition(level, pos, state, LilyPadPiece.NORTH_WEST) ? state : null;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (state.getValue(PIECE) == LilyPadPiece.NORTH_WEST) {
			placeAt(level, pos, state, LilyPadPiece.NORTH_WEST, false);
		}
	}

	public static boolean isValidPlacementPosition(LevelReader level, BlockPos pos, BlockState state, LilyPadPiece piece) {
		for (BlockPos blockPos : piece.getAllPositions(pos)) {
			if (!state.canSurvive(level, blockPos) || !level.isEmptyBlock(blockPos)) {
				return false;
			}
		}

		return true;
	}

	public static void placeAt(LevelWriter level, BlockPos pos, BlockState state, LilyPadPiece piece, boolean includeNorthWest) {
		BlockPos northWest = piece.getNorthWest(pos);

		if (includeNorthWest) {
			level.setBlock(northWest, state, UPDATE_ALL_IMMEDIATE);
		}

		level.setBlock(northWest.east(), state.setValue(PIECE, LilyPadPiece.NORTH_EAST), UPDATE_ALL_IMMEDIATE);
		level.setBlock(northWest.south(), state.setValue(PIECE, LilyPadPiece.SOUTH_WEST), UPDATE_ALL_IMMEDIATE);
		level.setBlock(northWest.south().east(), state.setValue(PIECE, LilyPadPiece.SOUTH_EAST), UPDATE_ALL_IMMEDIATE);
	}

	public static BlockState getBaseState(Direction facing) {
		return HollowBlocks.GIANT_LILY_PAD.defaultBlockState().setValue(FACING, facing);
	}

	public static boolean tryForm(BlockPlaceContext context, BlockState placementState) {
		if (!placementState.is(HollowBlockItemTags.FORMS_GIANT_LILY_PAD.block()) || context.isSecondaryUseActive())
			return false;

		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		BlockState blockState = getBaseState(context.getHorizontalDirection());

		for (LilyPadPiece piece : LilyPadPiece.values()) {
			placeAllPieces:
			for (int i = 0; i < 4; i++) {
				for (BlockPos blockPos : piece.getAllPositions(clickedPos)) {
					if (!blockPos.equals(clickedPos) && !level.getBlockState(blockPos).is(HollowBlockItemTags.FORMS_GIANT_LILY_PAD.block())) {
						continue placeAllPieces;
					}
				}

				placeAt(level, clickedPos, blockState, piece, true);

				return true;
			}
		}

		return false;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
		super.affectNeighborsAfterRemoval(state, level, pos, moved);

		for (BlockPos blockPos : state.getValue(PIECE).getAllPositions(pos)) {
			if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
				level.destroyBlock(blockPos, false);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PIECE, FACING);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
}
