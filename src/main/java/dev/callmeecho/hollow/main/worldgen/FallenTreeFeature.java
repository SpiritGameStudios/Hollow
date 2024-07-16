package dev.callmeecho.hollow.main.worldgen;

import com.mojang.serialization.Codec;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.block.PolyporeBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
        BlockState state = context.getConfig().stateProvider().get(random, origin);
        
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
            canPlace = canPlace && (context.getWorld().isAir(pos) || context.getWorld().getBlockState(pos).isOf(Blocks.SHORT_GRASS)) && context.getWorld().getBlockState(pos.down()).isSolidBlock(context.getWorld(), pos.down());
        }
        
        if (!canPlace) return false;
        
        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            context.getWorld().setBlockState(pos, state, 2);
            
            if (context.getWorld().isAir(pos.up()) && random.nextInt(2) == 0 && context.getConfig().mossy()) {
                context.getWorld().setBlockState(pos.up(), Blocks.MOSS_CARPET.getDefaultState(), 2);
                context.getWorld().setBlockState(pos, state.with(HollowLogBlock.MOSSY, true), 2);
            }
            
            Direction direction = switch (axis) {
                case X -> random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
                case Z -> random.nextBoolean() ? Direction.EAST : Direction.WEST;
                default -> Direction.fromHorizontal(random.nextInt(4));
            };
            BlockPos polyporePos = pos.offset(direction);
            
            if (random.nextInt(2) != 0 || !context.getWorld().isAir(polyporePos) || !context.getConfig().polypore()) continue;
            
            BlockState polyporeState = HollowBlockRegistry.POLYPORE.getDefaultState()
                    .with(Properties.HORIZONTAL_FACING, direction)
                    .with(PolyporeBlock.POLYPORE_AMOUNT, random.nextBetween(1, 3));
            context.getWorld().setBlockState(polyporePos, polyporeState, 2);
        }
        
        return true;
    }
}
