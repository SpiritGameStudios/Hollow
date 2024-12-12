package dev.spiritstudios.hollow.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.exception.UnreachableException;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class FallenTreeFeature extends Feature<FallenTreeFeature.Config> {
    public FallenTreeFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<Config> context) {
        BlockPos origin = context.getOrigin();
        StructureWorldAccess world = context.getWorld();

        origin = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, origin);

        Random random = context.getRandom();
        BlockState state = context.getConfig().stateProvider().get(random, origin);
        Config config = context.getConfig();

        int size = config.baseHeight + random.nextInt(config.variance);

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        state = state.withIfExists(Properties.AXIS, axis);


        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            if ((!world.isAir(pos) && !world.getBlockState(pos).isReplaceable()) || !world.getBlockState(pos.down()).isSolidBlock(world, pos.down()))
                return false;
        }

        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.offset(axis, i);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);

            if (world.isAir(pos.up())) {
                BlockState top = config.topBlockProvider().get(random, pos.up());
                world.setBlockState(pos.up(), top, Block.NOTIFY_ALL);
                if (top.isOf(Blocks.MOSS_CARPET))
                    world.setBlockState(pos, state.withIfExists(HollowLogBlock.MOSSY, true), Block.NOTIFY_ALL);
            }

            Direction direction = switch (axis) {
                case X -> random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
                case Z -> random.nextBoolean() ? Direction.EAST : Direction.WEST;
                default -> throw new UnreachableException();
            };

            BlockPos sidePos = pos.offset(direction);
            BlockState sideState = world.getBlockState(sidePos);
            if (!sideState.isAir() && !sideState.isReplaceable()) continue;
            if (sideState.isOf(Blocks.TALL_GRASS) || sideState.isIn(BlockTags.TALL_FLOWERS)) continue;

            world.setBlockState(
                    sidePos,
                    config.sideBlockProvider.get(random, sidePos)
                            .withIfExists(Properties.HORIZONTAL_FACING, direction)
                            .withIfExists(Properties.NORTH, direction == Direction.SOUTH)
                            .withIfExists(Properties.SHORT, direction == Direction.NORTH)
                            .withIfExists(Properties.EAST, direction == Direction.WEST)
                            .withIfExists(Properties.WEST, direction == Direction.EAST),
                    Block.NOTIFY_ALL
            );
        }

        return true;
    }

    public record Config(
            BlockStateProvider stateProvider,
            int baseHeight,
            int variance,
            BlockStateProvider topBlockProvider,
            BlockStateProvider sideBlockProvider
    ) implements FeatureConfig {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(Config::stateProvider),
                Codec.INT.optionalFieldOf("base_height", 3).forGetter(Config::baseHeight),
                Codec.INT.optionalFieldOf("variance", 2).forGetter(Config::variance),
                BlockStateProvider.TYPE_CODEC.fieldOf("top_block_provider").forGetter(Config::topBlockProvider),
                BlockStateProvider.TYPE_CODEC.fieldOf("side_block_provider").forGetter(Config::sideBlockProvider)
        ).apply(instance, Config::new));
    }
}
