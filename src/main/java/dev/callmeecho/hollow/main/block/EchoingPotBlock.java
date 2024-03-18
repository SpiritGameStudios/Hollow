package dev.callmeecho.hollow.main.block;

import dev.callmeecho.hollow.main.block.entity.EchoingPotBlockEntity;
import dev.callmeecho.hollow.main.registry.HollowBlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EchoingPotBlock extends BlockWithEntity {
    public EchoingPotBlock(Settings settings) {
        super(settings);
    }
    
    public static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 1, 15, 13, 15),
            Block.createCuboidShape(3, 13, 3, 13, 15, 13)
    );

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoingPotBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        EchoingPotBlockEntity blockEntity = (EchoingPotBlockEntity)world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).use(player, hand);
        return ActionResult.CONSUME;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, HollowBlockEntityRegistry.ECHOING_POT_BLOCK_ENTITY, EchoingPotBlockEntity::tick);
    }
}
