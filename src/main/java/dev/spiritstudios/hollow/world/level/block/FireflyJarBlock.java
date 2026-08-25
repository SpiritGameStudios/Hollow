package dev.spiritstudios.hollow.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import dev.spiritstudios.hollow.world.level.block.entity.FireflyJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class FireflyJarBlock extends BaseJarBlock {
	public static final MapCodec<FireflyJarBlock> CODEC = simpleCodec(FireflyJarBlock::new);

    public FireflyJarBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

	private static boolean hasJebName(Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof FireflyJarBlockEntity blockEntity && blockEntity.isJeb();
	}

	@Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		AABB shape = BODY_SHAPE.bounds().inflate(-0.0625);

		float x = Mth.randomBetween(random, (float) shape.minX, (float) shape.maxX);
		float y = Mth.randomBetween(random, (float) shape.minY, (float) shape.maxY);
		float z = Mth.randomBetween(random, (float) shape.minZ, (float) shape.maxZ);

		if (state.getValue(HANGING))
			y += HANGING_SHAPE_Y_DIFF;

		double isJeb = hasJebName(level, pos) ? 1.0 : 0.0;
		level.addParticle(HollowParticleTypes.JAR_FIREFLY, pos.getX() + x, pos.getY() + y, pos.getZ() + z, isJeb, 0.0, 0.0);
	}

	@Override
	protected MapCodec<FireflyJarBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
		return new FireflyJarBlockEntity(worldPosition, blockState);
	}
}
