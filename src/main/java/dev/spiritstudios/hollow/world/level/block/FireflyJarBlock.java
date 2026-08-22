package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(30) == 0
                && level.environmentAttributes().getValue(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, pos)
                && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos) <= pos.getY()) {
            level.playLocalSound(pos, SoundEvents.FIREFLY_BUSH_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
        }

        if (level.getMaxLocalRawBrightness(pos) <= 13 && random.nextDouble() <= 0.7) {
            level.addParticle(HollowParticleTypes.JAR_FIREFLY, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return JarBlock.SHAPE;
    }
}
