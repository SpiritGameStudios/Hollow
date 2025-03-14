package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.specter.api.core.math.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScreamingVaseBlock extends BlockWithEntity {
    public static final MapCodec<ScreamingVaseBlock> CODEC = createCodec(ScreamingVaseBlock::new);

    public static final DirectionProperty FACING = Properties.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    
    public ScreamingVaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER));
    }

    public static final VoxelShape LOWER_SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 16, 14);

    public static final VoxelShape UPPER_SHAPE_NS = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 13, 14),
            Block.createCuboidShape(5, 13, 5, 11, 16, 11),
            Block.createCuboidShape(0, 7, 7, 16, 10, 9)
    );

    public static final VoxelShape UPPER_SHAPE_EW = VoxelShapeHelper.rotateHorizontal(Direction.EAST, Direction.NORTH, UPPER_SHAPE_NS);



    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.UPPER ?
                state.get(FACING).getAxis() == Direction.Axis.X ? UPPER_SHAPE_EW : UPPER_SHAPE_NS :
                LOWER_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoingVaseBlockEntity(pos, state);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ItemActionResult.SUCCESS;

        if (state.get(HALF) == DoubleBlockHalf.UPPER)
            pos = pos.down();

        EchoingVaseBlockEntity blockEntity = (EchoingVaseBlockEntity) world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).use(player, hand);
        return ItemActionResult.CONSUME;
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        EchoingVaseBlockEntity blockEntity = (EchoingVaseBlockEntity) world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).onEntityCollision(state, world, pos, entity);
    }

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, HollowBlockEntityTypes.ECHOING_VASE, EchoingVaseBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(FACING);
        builder.add(HALF);
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        return pos.getY() < world.getTopY() - 1 && world.getBlockState(pos.up()).canReplace(ctx) ?
                getDefaultState()
                        .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()): null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            BlockState lower = world.getBlockState(pos.down());
            return lower.isOf(this) && lower.get(HALF) == DoubleBlockHalf.LOWER;
        }

        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient()) return super.onBreak(world, pos, state, player);

        if (state.get(HALF) == DoubleBlockHalf.LOWER)
            onBreakLower(world, pos, state, player);
        else
            onBreakLower(world, pos.down(), world.getBlockState(pos.down()), player);

        if (player.isCreative()) onBreakInCreative(world, pos, state, player);
        else dropStacks(state, world, pos, null, player, player.getMainHandStack());

        return super.onBreak(world, pos, state, player);
    }

    private static void onBreakLower(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient()) return;

        ((ServerWorld) world).spawnParticles(
                HollowParticleTypes.SCREAM,
                pos.getX(), pos.getY(), pos.getZ(),
                1, 0, 0, 0, 0
        );

        world.playSound(null, pos, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        world.emitGameEvent(GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos, GameEvent.Emitter.of(player));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, tool);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.get(HALF);

        if (direction.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP)) {
            return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)
                    ? Blocks.AIR.getDefaultState()
                    : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }

        return neighborState.isOf(this) && neighborState.get(HALF) != half
                ? neighborState.with(HALF, half)
                : Blocks.AIR.getDefaultState();
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        if (half == DoubleBlockHalf.LOWER) return;

        BlockPos lowerPos = pos.offset(state.get(FACING).rotateYClockwise());
        BlockState lower = world.getBlockState(lowerPos);

        if (!lower.isOf(state.getBlock()) || lower.get(HALF) != DoubleBlockHalf.LOWER)
            return;

        BlockState lowerFluid = lower.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        world.setBlockState(lowerPos, lowerFluid, Block.NOTIFY_ALL | Block.SKIP_DROPS);
        world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, lowerPos, Block.getRawIdFromState(lower));
    }

    @Override
    protected MapCodec<ScreamingVaseBlock> getCodec() {
        return CODEC;
    }
}
