package dev.spiritstudios.hollow.client.sound;

import net.minecraft.util.Mth;

public enum PitchInterpolationType {
	VELOCITY,
    RANDOM;

	float getPitch(MovingEntitySoundInstance<?> soundInstance, float velocity) {
		float delta = switch (this) {
			case VELOCITY -> Mth.clamp(velocity, soundInstance.getMinPitch(), soundInstance.getMaxPitch());
			case RANDOM -> soundInstance.getRandom().nextFloat();
		};

		return Mth.lerp(delta, soundInstance.getMinPitch(), soundInstance.getMaxPitch());
	}
}
