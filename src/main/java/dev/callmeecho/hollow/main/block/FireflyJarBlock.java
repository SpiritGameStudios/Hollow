package dev.callmeecho.hollow.main.block;

import dev.callmeecho.hollow.main.block.entity.FireflyJarBlockEntity;
import dev.callmeecho.hollow.main.block.entity.JarBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FireflyJarBlock extends BlockWithEntity {
    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(5.5, 14, 5.5, 10.5, 16, 10.5),
            createCuboidShape(3.5, 0, 3.5, 12.5, 14, 4.5),
            createCuboidShape(3.5, 0, 4.5, 4.5, 14, 11.5),
            createCuboidShape(3.5, 0, 11.5, 12.5, 14, 12.5),
            createCuboidShape(11.5, 0, 4.5, 12.5, 14, 11.5),
            createCuboidShape(4.5, 13, 4.5, 11.5, 14, 11.5),
            createCuboidShape(4.5, 0, 4.5, 11.5, 1, 11.5)
    );
    
    public FireflyJarBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireflyJarBlockEntity(pos, state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FireflyJarBlockEntity && random.nextInt(15) == 0) {
            ((FireflyJarBlockEntity)blockEntity).createParticles(world, pos, random);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
