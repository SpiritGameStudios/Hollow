package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FireflyJarBlock extends Block {
    public FireflyJarBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(15) == 0) {
			world.addParticle(
					ParticleTypes.FIREFLY,
					(pos.getX() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
					(pos.getY() + 0.5) + (2 * random.nextDouble() - 1) / 5.0F,
					(pos.getZ() + 0.5) + (random.nextDouble() - 0.5) / 5.0F,
					0, 0, 0
			);
		}
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return JarBlock.SHAPE;
    }
}
