package dev.spiritstudios.hollow.world.level.block.entity.pot;

import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ObaboBlockEntity extends PotBlockEntity {
	public ObaboBlockEntity(BlockPos pos, BlockState state) {
		super(HollowBlockEntityTypes.OBABO, pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, ObaboBlockEntity entity) {
		Vec3 centre = Vec3.atCenterOf(pos);

		final float radius = 2;

		level.addParticle(
			ParticleTypes.END_ROD,
			centre.x + Mth.sin(level.getGameTime() / 20F) * radius,
			centre.y,
			centre.z + Mth.cos(level.getGameTime() / 20F) * radius,
			0, 0, 0
		);
	}
}
