package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.world.level.block.entity.StoneChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class StoneChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ChestType> CHEST_TYPE = BlockStateProperties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final MapCodec<StoneChestBlock> CODEC = simpleCodec(StoneChestBlock::new);

    public static final VoxelShape SHAPE_SINGLE = Shapes.or(
            box(1, 1, 1, 15, 16, 15),
            box(0, 0, 0, 16, 1, 16)
    );

    private static final Map<Direction, VoxelShape> DOUBLE_LEFT_SHAPES_BY_DIRECTION = Shapes.rotateHorizontal(Shapes.or(
            box(1, 1, 1, 16, 16, 15),
            box(0, 0, 0, 16, 1, 16)
    ));

    private static final Map<Direction, VoxelShape> DOUBLE_RIGHT_SHAPES_BY_DIRECTION = Shapes.rotateHorizontal(Shapes.or(
            box(0, 1, 1, 15, 16, 15),
            box(0, 0, 0, 16, 1, 16)
    ));

    public StoneChestBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CHEST_TYPE, ChestType.SINGLE)
                .setValue(WATERLOGGED, false)
        );
    }

    public static Direction getFacing(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(CHEST_TYPE) == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        ChestType chestType = ChestType.SINGLE;
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        boolean shouldCancel = ctx.isSecondaryUseActive();
        Direction side = ctx.getClickedFace();

        if (side.getAxis().isHorizontal() && shouldCancel) {
            Direction neighborDirection = this.getNeighborChestDirection(ctx, side.getOpposite());
            if (neighborDirection != null && neighborDirection.getAxis() != side.getAxis()) {
                direction = neighborDirection;
                chestType = neighborDirection.getCounterClockWise() == side.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chestType == ChestType.SINGLE && !shouldCancel)
            if (direction == this.getNeighborChestDirection(ctx, direction.getClockWise()))
                chestType = ChestType.LEFT;
            else if (direction == this.getNeighborChestDirection(ctx, direction.getCounterClockWise()))
                chestType = ChestType.RIGHT;

        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(CHEST_TYPE, chestType)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Nullable
    private Direction getNeighborChestDirection(BlockPlaceContext ctx, Direction dir) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(dir));
        return blockState.is(this) && blockState.getValue(CHEST_TYPE) == ChestType.SINGLE ? blockState.getValue(FACING) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StoneChestBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CHEST_TYPE, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        if (direction == Direction.UP && neighborState.isAir()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoneChestBlockEntity stoneChest) {
                stoneChest.aboveBroken();
            }
        }

        if (neighborState.is(this) && direction.getAxis().isHorizontal()) {
            ChestType chestType = neighborState.getValue(CHEST_TYPE);
            if (state.getValue(CHEST_TYPE) == ChestType.SINGLE
                    && chestType != ChestType.SINGLE
                    && state.getValue(FACING) == neighborState.getValue(FACING)
                    && getFacing(neighborState) == direction.getOpposite()) {
                return state.setValue(CHEST_TYPE, chestType.getOpposite());
            }
        } else if (getFacing(state) == direction) {
            return state.setValue(CHEST_TYPE, ChestType.SINGLE);
        }

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        StoneChestBlockEntity blockEntity = (StoneChestBlockEntity) world.getBlockEntity(pos);
        return Objects.requireNonNull(blockEntity).use(player, hand, hit.getDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(CHEST_TYPE)) {
			case LEFT -> DOUBLE_LEFT_SHAPES_BY_DIRECTION.get(state.getValue(FACING));
			case RIGHT -> DOUBLE_RIGHT_SHAPES_BY_DIRECTION.get(state.getValue(FACING));
			default -> SHAPE_SINGLE;
		};
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
