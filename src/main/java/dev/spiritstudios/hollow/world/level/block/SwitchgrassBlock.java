package dev.spiritstudios.hollow.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireflyBushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchgrassBlock extends FireflyBushBlock {
	public SwitchgrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.1F && level.environmentAttributes().getValue(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, pos)) {
			level.setBlockAndUpdate(pos, Blocks.FIREFLY_BUSH.defaultBlockState());
		}
	}
}
