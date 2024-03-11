package dev.callmeecho.hollow.main.block;

import dev.callmeecho.cabinetapi.client.particle.ParticleSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PotBlock extends Block {
    private final ParticleSystem particleSystem;
    
    public PotBlock(Settings settings) {
        super(settings);
        particleSystem = new ParticleSystem(
                new Vec3d(0.007F, 0.07F, 0.007F),
                new Vec3d(0.5, 1, 0.5),
                new Vec3d(0.1, 0, 0.1),
                2,
                1,
                true,
                ParticleTypes.SCULK_SOUL
        );
    }
    
    public static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 0, 1, 15, 13, 15),
            Block.createCuboidShape(3, 13, 3, 13, 15, 13)
    );

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        particleSystem.tick(world, pos);
        //        for(int i = 0; i < 2; ++i) {
//            float x = 2.0F * random.nextFloat() - 1.0F;
//            float y = 2.0F * random.nextFloat() - 1.0F;
//            float z = 2.0F * random.nextFloat() - 1.0F;
//            world
//                    .addParticle(
//                            ParticleTypes.SCULK_SOUL,
//                            (double)pos.getX() + 0.5 + (x * 0.1),
//                            (double)pos.getY() + 1,
//                            (double)pos.getZ() + 0.5 + (z * 0.1),
//                            (x * 0.007F),
//                            (y * 0.07F),
//                            (z * 0.007F)
//                    );
//        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
