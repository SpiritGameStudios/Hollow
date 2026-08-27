package dev.spiritstudios.hollow.client.color.block;

import dev.spiritstudios.hollow.world.level.block.NewCattailBlock;
import dev.spiritstudios.hollow.world.level.block.state.properties.TripleBlockThird;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Set;

public final class HollowBlockTintSources {
	private HollowBlockTintSources() {}

	public static BlockTintSource cattail() {
		return new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return GrassColor.getDefaultColor();
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				BlockPos blockPos = pos;
				TripleBlockThird third = state.getValue(NewCattailBlock.THIRD);

				if (third != TripleBlockThird.LOWER)
					blockPos = pos.below(third == TripleBlockThird.UPPER ? 2 : 1);

				return BiomeColors.getAverageGrassColor(level, blockPos);
			}

			@Override
			public Set<Property<?>> relevantProperties() {
				return Set.of(NewCattailBlock.THIRD);
			}
		};
	}
}
