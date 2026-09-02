package dev.spiritstudios.hollow.client.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class MovingEntitySoundInstanceBuilder<T extends Entity> {
	private SoundEvent soundEvent = SoundEvents.EMPTY;
	private SoundSource soundSource = SoundSource.MASTER;
	private PitchInterpolationType pitchInterpolationType = PitchInterpolationType.VELOCITY;
	private float minPitch = 1.0F;
	private float maxPitch = 1.0F;
	private float minVolume = 0.0F;
	private float maxVolume = 1.0F;
	private float maxLerpSpeed = 0.0F;
	private MovingPredicate<T> movingPredicate = (_, _) -> true;

	public MovingEntitySoundInstanceBuilder<T> soundEvent(SoundEvent soundEvent) {
		this.soundEvent = soundEvent;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> soundSource(SoundSource soundSource) {
		this.soundSource = soundSource;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> pitchInterpolationType(PitchInterpolationType pitchInterpolationType) {
		this.pitchInterpolationType = pitchInterpolationType;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> pitchRange(float min, float max) {
		this.minPitch = min;
		this.maxPitch = max;
		return this;
	}

	public MovingEntitySoundInstanceBuilder<T> maxLerpSpeed(float speed) {
		this.maxLerpSpeed = speed;
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

	public MovingEntitySoundInstanceBuilder<T> movingPredicate(MovingPredicate<T> movingPredicate) {
		this.movingPredicate = movingPredicate;
		return this;
	}

	public MovingEntitySoundInstance<T> buildAndApplyTo(T entity) {
		return new MovingEntitySoundInstance<>(
			entity,
			this.soundEvent,
			this.soundSource,
			this.pitchInterpolationType,
			this.minPitch,
			this.maxPitch,
			this.minVolume,
			this.maxVolume,
			this.maxLerpSpeed,
			this.movingPredicate
		);
	}
}
