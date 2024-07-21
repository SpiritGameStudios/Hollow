package dev.callmeecho.hollow.main.block;

import dev.callmeecho.cabinetapi.particle.ParticleSystem;
import dev.callmeecho.hollow.main.registry.HollowParticleRegistrar;
import net.minecraft.block.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class FireflyJarBlock extends Block {
    private final ParticleSystem particleSystem;

    public FireflyJarBlock(AbstractBlock.Settings settings) {
        super(settings);
        particleSystem = new ParticleSystem(
                new Vec3d(0, 0, 0),
                new Vec3d(0.5, 0.45, 0.5),
                new Vec3d(0.15, 0.3, 0.15),
                1,
                15,
                false,
                HollowParticleRegistrar.FIREFLY_JAR);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        particleSystem.tick(world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return JarBlock.SHAPE; }
}
