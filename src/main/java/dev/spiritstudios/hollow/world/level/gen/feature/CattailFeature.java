package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.world.level.block.CattailBlock;
import dev.spiritstudios.hollow.world.level.block.CattailStemBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CattailFeature extends Feature<NoneFeatureConfiguration> {
    public CattailFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        WorldGenLevel world = context.level();

        BlockPos pos = origin.atY(world.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ()));
        if (!HollowBlocks.CATTAIL.defaultBlockState().canSurvive(world, pos)) return false;
        if (!world.isWaterAt(pos)) return false;

        world.setBlock(
                pos,
                HollowBlocks.CATTAIL_STEM.defaultBlockState().setValue(CattailStemBlock.BOTTOM, true),
                Block.UPDATE_CLIENTS
        );
        pos = pos.above();

        while (world.isWaterAt(pos)) {
            world.setBlock(pos, HollowBlocks.CATTAIL_STEM.defaultBlockState(), Block.UPDATE_CLIENTS);
            pos = pos.above();
        }

        int height = random.nextIntBetweenInclusive(1, 4);

        for (int i = 0; i < height; i++) {
            world.setBlock(pos, HollowBlocks.CATTAIL_STEM.defaultBlockState().setValue(CattailBlock.WATERLOGGED, false), Block.UPDATE_ALL);
            pos = pos.above();
        }

        world.setBlock(pos, HollowBlocks.CATTAIL.defaultBlockState().setValue(CattailBlock.WATERLOGGED, false), Block.UPDATE_ALL);

        return true;
    }
}
