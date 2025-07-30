package dev.spiritstudios.hollow.worldgen.feature;

import dev.spiritstudios.hollow.block.CattailBlock;
import dev.spiritstudios.hollow.block.CattailStemBlock;
import dev.spiritstudios.hollow.block.HollowBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CattailFeature extends Feature<DefaultFeatureConfig> {
    public CattailFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();

        BlockPos pos = origin.withY(world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX(), origin.getZ()));
        if (!HollowBlocks.CATTAIL.getDefaultState().canPlaceAt(world, pos)) return false;
        if (!world.isWater(pos)) return false;

        world.setBlockState(
                pos,
                HollowBlocks.CATTAIL_STEM.getDefaultState().with(CattailStemBlock.BOTTOM, true),
                Block.NOTIFY_LISTENERS
        );
        pos = pos.up();

        while (world.isWater(pos)) {
            world.setBlockState(pos, HollowBlocks.CATTAIL_STEM.getDefaultState(), Block.NOTIFY_LISTENERS);
            pos = pos.up();
        }

        int height = random.nextBetween(1, 4);

        for (int i = 0; i < height; i++) {
            world.setBlockState(pos, HollowBlocks.CATTAIL_STEM.getDefaultState().with(CattailBlock.WATERLOGGED, false), Block.NOTIFY_ALL);
            pos = pos.up();
        }

        world.setBlockState(pos, HollowBlocks.CATTAIL.getDefaultState().with(CattailBlock.WATERLOGGED, false), Block.NOTIFY_ALL);

        return true;
    }
}
