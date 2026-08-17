package dev.spiritstudios.hollow.world.level.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class FallenTreeFeature extends Feature<FallenTreeFeature.Config> {
    public FallenTreeFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        origin = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, origin);

        RandomSource random = context.random();
        BlockState state = context.config().stateProvider().getState(level, random, origin);
        Config config = context.config();

        int size = config.baseHeight + random.nextInt(config.variance);

        Direction.Axis axis = random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        state = state.trySetValue(BlockStateProperties.AXIS, axis);


        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.relative(axis, i);
            if ((!level.isEmptyBlock(pos) && !level.getBlockState(pos).canBeReplaced()) || !level.getBlockState(pos.below()).isRedstoneConductor(level, pos.below()))
                return false;
        }

        for (int i = 0; i < size; i++) {
            BlockPos pos = origin.relative(axis, i);
            level.setBlock(pos, state, Block.UPDATE_ALL);

            if (level.isEmptyBlock(pos.above())) {
                BlockState top = config.topBlockProvider().getState(level, random, pos.above());
                level.setBlock(pos.above(), top, Block.UPDATE_ALL);

                level.setBlock(pos, state.trySetValue(HollowLogBlock.LAYER, HollowLogBlock.Layer.get(top)), Block.UPDATE_ALL);
            }

            Direction direction = switch (axis) {
                case X -> random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
                case Z -> random.nextBoolean() ? Direction.EAST : Direction.WEST;
                default -> throw new IllegalStateException();
            };

            BlockPos sidePos = pos.relative(direction);
            BlockState sideState = level.getBlockState(sidePos);
            if (!sideState.isAir() && !sideState.canBeReplaced()) continue;
            if (sideState.is(Blocks.TALL_GRASS) || sideState.is(ConventionalBlockTags.TALL_FLOWERS)) continue;

            level.setBlock(
                    sidePos,
                    config.sideBlockProvider.getState(level, random, sidePos)
                            .trySetValue(BlockStateProperties.HORIZONTAL_FACING, direction)
                            .trySetValue(BlockStateProperties.NORTH, direction == Direction.SOUTH)
                            .trySetValue(BlockStateProperties.SHORT, direction == Direction.NORTH)
                            .trySetValue(BlockStateProperties.EAST, direction == Direction.WEST)
                            .trySetValue(BlockStateProperties.WEST, direction == Direction.EAST),
                    Block.UPDATE_ALL
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
    ) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(Config::stateProvider),
                Codec.INT.optionalFieldOf("base_height", 3).forGetter(Config::baseHeight),
                Codec.INT.optionalFieldOf("variance", 2).forGetter(Config::variance),
                BlockStateProvider.CODEC.fieldOf("top_block_provider").forGetter(Config::topBlockProvider),
                BlockStateProvider.CODEC.fieldOf("side_block_provider").forGetter(Config::sideBlockProvider)
        ).apply(instance, Config::new));
    }
}
