package dev.callmeecho.hollow.main.block;

import com.mojang.serialization.MapCodec;
import dev.callmeecho.hollow.main.block.entity.JarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class JarBlock extends BlockWithEntity {
    public static final MapCodec<JarBlock> CODEC = createCodec(JarBlock::new);

    public JarBlock(AbstractBlock.Settings settings) {
        super(settings);
    }
    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(5.5, 14, 5.5, 10.5, 16, 10.5),
            createCuboidShape(3.5, 0, 3.5, 12.5, 14, 4.5),
            createCuboidShape(3.5, 0, 4.5, 4.5, 14, 11.5),
            createCuboidShape(3.5, 0, 11.5, 12.5, 14, 12.5),
            createCuboidShape(11.5, 0, 4.5, 12.5, 14, 11.5),
            createCuboidShape(4.5, 13, 4.5, 11.5, 14, 11.5),
            createCuboidShape(4.5, 0, 4.5, 11.5, 1, 11.5)
    );

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }


    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ItemActionResult.SUCCESS;

        JarBlockEntity blockEntity = (JarBlockEntity)world.getBlockEntity(pos);
        Objects.requireNonNull(blockEntity).use(world, pos, player, hand);
        return ItemActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JarBlockEntity) {
                ItemScatterer.spawn(world, pos, (JarBlockEntity)blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }
}
