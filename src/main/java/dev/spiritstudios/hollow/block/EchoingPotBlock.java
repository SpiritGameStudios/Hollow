package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.block.entity.EchoingPotBlockEntity;
import dev.spiritstudios.hollow.block.entity.HollowBlockEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EchoingPotBlock extends BlockWithEntity {
    public static final MapCodec<EchoingPotBlock> CODEC = createCodec(EchoingPotBlock::new);

    public EchoingPotBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 1, 15, 14, 15),
            Block.createCuboidShape(4, 14, 4, 12, 16, 12)
    );

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoingPotBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.SUCCESS;

        EchoingPotBlockEntity blockEntity = (EchoingPotBlockEntity) world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).use(player, hand);
        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, HollowBlockEntityTypes.ECHOING_POT, EchoingPotBlockEntity::tick);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
}
