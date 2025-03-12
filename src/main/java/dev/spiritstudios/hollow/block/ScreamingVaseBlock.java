package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScreamingVaseBlock extends VerticalDoubleBlock implements BlockEntityProvider {
    public static final MapCodec<ScreamingVaseBlock> CODEC = createCodec(ScreamingVaseBlock::new);

    public ScreamingVaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static final VoxelShape LOWER_SHAPE =
            Block.createCuboidShape(2, 0, 2, 14, 16, 14);

    public static final VoxelShape UPPER_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 0, 2, 14, 9, 14),
            Block.createCuboidShape(4, 4, 4, 12, 8, 12)
    );

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoingVaseBlockEntity(pos, state);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ItemActionResult.SUCCESS;

        if (state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)
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
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    protected MapCodec<ScreamingVaseBlock> getCodec() {
        return CODEC;
    }
}
