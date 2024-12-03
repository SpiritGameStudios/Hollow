package dev.spiritstudios.hollow.worldgen.feature;

import com.mojang.serialization.Codec;
import dev.spiritstudios.hollow.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class GiantLilypadFeature extends Feature<DefaultFeatureConfig> {
    public GiantLilypadFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        StructureWorldAccess world = context.getWorld();

        BlockPos pos = origin.add(
                random.nextInt(16) - 8,
                0,
                random.nextInt(16) - 8
        );

        if (!canPlaceAt(world, pos)) return false;

        Direction facing = Direction.fromHorizontal(random.nextInt(4));
        BlockState lilypadState = HollowBlocks.GIANT_LILYPAD.getDefaultState().with(GiantLilyPadBlock.FACING, facing);

        world.setBlockState(pos, lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_WEST), Block.NOTIFY_ALL_AND_REDRAW);
        world.setBlockState(pos.east(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.NORTH_EAST), Block.NOTIFY_ALL_AND_REDRAW);
        world.setBlockState(pos.south(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_WEST), Block.NOTIFY_ALL_AND_REDRAW);
        world.setBlockState(pos.east().south(), lilypadState.with(GiantLilyPadBlock.PIECE, GiantLilyPadBlock.Piece.SOUTH_EAST), Block.NOTIFY_ALL_AND_REDRAW);

        return true;
    }

    private boolean canPlaceAt(StructureWorldAccess world, BlockPos pos) {
        return world.isAir(pos) && world.getBlockState(pos.down()).isOf(Blocks.WATER) &&
                world.isAir(pos.east()) && world.getBlockState(pos.east().down()).isOf(Blocks.WATER) &&
                world.isAir(pos.south()) && world.getBlockState(pos.south().down()).isOf(Blocks.WATER) &&
                world.isAir(pos.east().south()) && world.getBlockState(pos.east().south().down()).isOf(Blocks.WATER);
    }
}
