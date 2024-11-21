package dev.spiritstudios.hollow.worldgen.feature;

import com.mojang.serialization.Codec;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.specter.api.core.exception.UnreachableException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
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
        StructureWorldAccess world = context.getWorld();

        int size = random.nextBetween(3, 5);
        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        state = state.withIfExists(Properties.AXIS, axis);

        origin = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, origin);

        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            if ((!world.isAir(pos) && !world.getBlockState(pos).isReplaceable()) || !world.getBlockState(pos.down()).isSolidBlock(world, pos.down()))
                return false;
        }

        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);

            if (world.isAir(pos.up()) && random.nextInt(2) == 0 && context.getConfig().mossy()) {
                world.setBlockState(pos.up(), Blocks.MOSS_CARPET.getDefaultState(), 2);
                world.setBlockState(pos, state.with(HollowLogBlock.MOSSY, true), 2);
            }

            Direction direction = switch (axis) {
                case X -> random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
                case Z -> random.nextBoolean() ? Direction.EAST : Direction.WEST;
                default -> throw new UnreachableException();
            };

            if (!context.getConfig().polypore() || random.nextInt(2) != 0) continue;

            BlockPos polyporePos = pos.offset(direction);
            if (!world.isAir(polyporePos)) continue;

            world.setBlockState(
                    polyporePos,
                    HollowBlockRegistrar.POLYPORE.getDefaultState()
                            .with(Properties.HORIZONTAL_FACING, direction)
                            .with(PolyporeBlock.POLYPORE_AMOUNT, random.nextBetween(1, 3)),
                    Block.NOTIFY_LISTENERS
            );
        }

        return true;
    }
}
