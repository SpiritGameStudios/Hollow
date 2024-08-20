package dev.spiritstudios.hollow.block;

import dev.spiritstudios.hollow.registry.HollowParticleRegistrar;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FireflyJarBlock extends Block {
    public FireflyJarBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(15) == 0)
            world.addParticle(
                    HollowParticleRegistrar.FIREFLY_JAR,
                    (pos.getX() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
                    (pos.getY() + 0.5) + (2 * random.nextDouble() - 1) / 5.0F,
                    (pos.getZ() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
                    0,
                    0,
                    0);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return JarBlock.SHAPE;
    }
}
