package dev.callmeecho.hollow.worldgen;

import com.mojang.serialization.Codec;
import dev.callmeecho.hollow.block.CattailBlock;
import dev.callmeecho.hollow.registry.HollowBlockRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CattailFeature extends Feature<DefaultFeatureConfig> {
    public CattailFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();

        BlockPos pos = origin.withY(world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX(), origin.getZ()));
        if (!world.getBlockState(pos).isOf(Blocks.WATER)) return false;

        world.setBlockState(pos, HollowBlockRegistrar.CATTAIL.getDefaultState(), Block.NOTIFY_LISTENERS);
        pos = pos.up();

        while (world.isWater(pos)) {
            world.setBlockState(pos, HollowBlockRegistrar.CATTAIL.getDefaultState().with(CattailBlock.PIECE, CattailBlock.Piece.MIDDLE), Block.NOTIFY_LISTENERS);
            pos = pos.up();
        }

        int height = random.nextBetween(1, 3);

        for (int i = 0; i < height; i++) {
            world.setBlockState(pos, HollowBlockRegistrar.CATTAIL.getDefaultState().with(CattailBlock.PIECE, CattailBlock.Piece.MIDDLE).with(CattailBlock.WATERLOGGED, false), Block.NOTIFY_ALL);
            pos = pos.up();
        }

        world.setBlockState(pos, HollowBlockRegistrar.CATTAIL.getDefaultState().with(CattailBlock.PIECE, CattailBlock.Piece.TOP).with(CattailBlock.WATERLOGGED, false), Block.NOTIFY_ALL);

        return true;
    }
}
