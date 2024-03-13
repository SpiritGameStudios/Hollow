package dev.callmeecho.hollow.main.block;

import dev.callmeecho.cabinetapi.client.particle.ParticleSystem;
import dev.callmeecho.hollow.main.registry.HollowParticleRegistrar;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FireflyJarBlock extends Block {
    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(5.5, 14, 5.5, 10.5, 16, 10.5),
            createCuboidShape(3.5, 0, 3.5, 12.5, 14, 4.5),
            createCuboidShape(3.5, 0, 4.5, 4.5, 14, 11.5),
            createCuboidShape(3.5, 0, 11.5, 12.5, 14, 12.5),
            createCuboidShape(11.5, 0, 4.5, 12.5, 14, 11.5),
            createCuboidShape(4.5, 13, 4.5, 11.5, 14, 11.5),
            createCuboidShape(4.5, 0, 4.5, 11.5, 1, 11.5)
    );

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

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        particleSystem.tick(world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) { return SHAPE; }
}
