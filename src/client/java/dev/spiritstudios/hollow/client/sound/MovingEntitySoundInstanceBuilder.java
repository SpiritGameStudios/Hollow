package dev.spiritstudios.hollow.client.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

public class MovingEntitySoundInstanceBuilder<T extends Entity> {
	private SoundEvent soundEvent = SoundEvents.EMPTY;
	private SoundParameterInterpolationType soundParameterInterpolationType = SoundParameterInterpolationType.VELOCITY;
	private float minPitch = 1.0F;
	private float maxPitch = 1.0F;
	private float minVolume = 0.0F;
	private float maxVolume = 1.0F;
	private float maxParameterInterpolationSpeed = 0.0F;
	private MovingEntityPredicate<T> canPlaySound = (_, _) -> true;
	private MovingEntityPredicate<T> canInterpolateSoundParameters = (_, _) -> true;

	public MovingEntitySoundInstanceBuilder<T> soundEvent(SoundEvent soundEvent) {
		this.soundEvent = soundEvent;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> parameterInterpolationType(SoundParameterInterpolationType soundParameterInterpolationType) {
		this.soundParameterInterpolationType = soundParameterInterpolationType;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> pitch(float pitch) {
		return this.pitchRange(pitch, pitch);
	}

	public MovingEntitySoundInstanceBuilder<T> pitchRange(float min, float max) {
		this.minPitch = min;
		this.maxPitch = max;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> maxParameterInterpolationSpeed(float maxParameterInterpolationSpeed) {
		this.maxParameterInterpolationSpeed = maxParameterInterpolationSpeed;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> volume(float volume) {
		return this.volumeRange(volume, volume);
	}

	public MovingEntitySoundInstanceBuilder<T> volumeRange(float min, float max) {
		this.minVolume = min;
		this.maxVolume = max;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> canPlaySound(MovingEntityPredicate<T> canPlaySound) {
		this.canPlaySound = canPlaySound;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> canInterpolateSoundParameters(MovingEntityPredicate<T> canInterpolateSoundParameters) {
		this.canInterpolateSoundParameters = canInterpolateSoundParameters;
		return this;
	}

	public MovingEntitySoundInstance<T> buildAndApplyTo(T entity) {
		return new MovingEntitySoundInstance<>(
			entity,
			this.soundEvent,
			this.soundParameterInterpolationType,
			this.minPitch,
			this.maxPitch,
			this.minVolume,
			this.maxVolume,
			this.maxParameterInterpolationSpeed,
			this.canPlaySound,
			this.canInterpolateSoundParameters
		);
	}
}
