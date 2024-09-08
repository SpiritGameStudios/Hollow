package dev.spiritstudios.hollow.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.block.entity.JarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

public class JarBlock extends BlockWithEntity {
    public static final MapCodec<JarBlock> CODEC = createCodec(JarBlock::new);
    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(5.5, 12, 5.5, 10.5, 15, 10.5),
            createCuboidShape(3.5, 0, 3.5, 12.5, 14, 4.5),
            createCuboidShape(3.5, 0, 4.5, 4.5, 14, 11.5),
            createCuboidShape(3.5, 0, 11.5, 12.5, 14, 12.5),
            createCuboidShape(11.5, 0, 4.5, 12.5, 14, 11.5),
            createCuboidShape(4.5, 13, 4.5, 11.5, 14, 11.5),
            createCuboidShape(4.5, 0, 4.5, 11.5, 1, 11.5)
    );

    public JarBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

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

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) jarBlockEntity.use(world, pos, player, hand);
        return ItemActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() == newState.getBlock()) return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JarBlockEntity jarBlockEntity) ItemScatterer.spawn(world, pos, jarBlockEntity);

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
}
