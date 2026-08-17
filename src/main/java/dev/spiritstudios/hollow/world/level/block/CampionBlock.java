package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

public class CampionBlock extends TallFlowerBlock {
    private static final VoxelShape UPPER_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public CampionBlock(Properties settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ?
                UPPER_SHAPE :
                super.getShape(state, world, pos, context);
    }
}
