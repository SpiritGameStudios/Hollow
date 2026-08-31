package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.world.level.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.world.level.block.state.properties.LilyPadPiece;
import net.minecraft.world.level.block.state.BlockState;
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
        WorldGenLevel level = context.level();

        BlockPos pos = origin.offset(random.nextInt(16) - 8, 0, random.nextInt(16) - 8);
		BlockState blockState = GiantLilyPadBlock.getBaseState(Direction.Plane.HORIZONTAL.getRandomDirection(random));

		if (GiantLilyPadBlock.isValidPlacementPosition(level, pos, blockState, LilyPadPiece.NORTH_WEST)) {
			GiantLilyPadBlock.placeAt(level, pos, blockState, LilyPadPiece.NORTH_WEST, true);
			return true;
		}

		return false;
	}
}
