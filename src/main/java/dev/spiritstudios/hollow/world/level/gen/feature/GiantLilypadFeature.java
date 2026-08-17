package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.world.level.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class GiantLilypadFeature extends Feature<NoneFeatureConfiguration> {
    public GiantLilypadFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        WorldGenLevel world = context.level();

        BlockPos pos = origin.offset(
                random.nextInt(16) - 8,
                0,
                random.nextInt(16) - 8
        );

        if (!canPlaceAt(world, pos)) return false;

        Direction facing = Direction.from2DDataValue(random.nextInt(4));
        BlockState lilypadState = HollowBlocks.GIANT_LILY_PAD.defaultBlockState().setValue(GiantLilyPadBlock.FACING, facing);

        world.setBlock(pos, lilypadState.setValue(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_WEST), Block.UPDATE_ALL_IMMEDIATE);
        world.setBlock(pos.east(), lilypadState.setValue(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_EAST), Block.UPDATE_ALL_IMMEDIATE);
        world.setBlock(pos.south(), lilypadState.setValue(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_WEST), Block.UPDATE_ALL_IMMEDIATE);
        world.setBlock(pos.east().south(), lilypadState.setValue(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_EAST), Block.UPDATE_ALL_IMMEDIATE);

        return true;
    }

    private boolean canPlaceAt(WorldGenLevel world, BlockPos pos) {
        return world.isEmptyBlock(pos) && world.getBlockState(pos.below()).is(Blocks.WATER) &&
                world.isEmptyBlock(pos.east()) && world.getBlockState(pos.east().below()).is(Blocks.WATER) &&
                world.isEmptyBlock(pos.south()) && world.getBlockState(pos.south().below()).is(Blocks.WATER) &&
                world.isEmptyBlock(pos.east().south()) && world.getBlockState(pos.east().south().below()).is(Blocks.WATER);
    }
}
