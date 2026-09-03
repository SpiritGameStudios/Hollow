package dev.spiritstudios.hollow.client.sound;

public enum SoundParameterInterpolationType {
	VELOCITY,
    RANDOM;

	float getDeltaValue(MovingEntitySoundInstance<?> soundInstance, float velocity) {
		return !soundInstance.canInterpolateParameters() ? 0.0F : switch (this) {
			case VELOCITY -> Math.min(velocity / soundInstance.getMaxParameterInterpolationSpeed(), 1.0F);
			case RANDOM -> soundInstance.getRandom().nextFloat();
		};
	}
}
