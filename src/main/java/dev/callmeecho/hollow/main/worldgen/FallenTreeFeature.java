package dev.callmeecho.hollow.main.worldgen;

import com.mojang.serialization.Codec;
import dev.callmeecho.hollow.main.Hollow;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FallenTreeFeature extends Feature<FallenTreeFeatureConfig> {
    public FallenTreeFeature(Codec<FallenTreeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<FallenTreeFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BlockState state = context.getConfig().stateProvider.get(random, origin);
        
        int size = random.nextBetween(3, 5);
        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        if (state.contains(Properties.AXIS)) state = state.with(HollowLogBlock.AXIS, axis);
        
        for (int i = 0; i < 10; i++) {
            if (context.getWorld().isAir(origin.down())) {
                origin = origin.down();
            } else {
                break;
            }
        }
        
        boolean canPlace = true;
        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            canPlace = canPlace && (context.getWorld().isAir(pos) || context.getWorld().getBlockState(pos).isOf(Blocks.GRASS)) && context.getWorld().getBlockState(pos.down()).isSolidBlock(context.getWorld(), pos.down());
        }
        
        if (!canPlace) return false;
        
        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            context.getWorld().setBlockState(pos, state, 2);
            
            if (context.getWorld().isAir(pos.up()) && random.nextInt(2) == 0) {
                context.getWorld().setBlockState(pos.up(), Blocks.MOSS_CARPET.getDefaultState(), 2);
            }
        }
        
        return true;
    }
}
