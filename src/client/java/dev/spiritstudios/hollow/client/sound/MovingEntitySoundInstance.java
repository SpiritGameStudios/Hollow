package dev.spiritstudios.hollow.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public final class MovingEntitySoundInstance<T extends Entity> extends AbstractTickableSoundInstance {
    final T entity;
    private final SoundParameterInterpolationType soundParameterInterpolationType;
    private final float minPitch;
    private final float maxPitch;
    private final float minVolume;
    private final float maxVolume;
	private final float maxParameterInterpolationSpeed;

	private final MovingEntityPredicate<T> canPlaySound, canInterpolateParameters;

	public MovingEntitySoundInstance(
		T entity,
		SoundEvent soundEvent,
		SoundParameterInterpolationType soundParameterInterpolationType,
		float minPitch,
		float maxPitch,
		float minVolume,
		float maxVolume,
		float maxParameterInterpolationSpeed,
		MovingEntityPredicate<T> canPlaySound,
		MovingEntityPredicate<T> canInterpolateParameters
	) {
		super(soundEvent, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.soundParameterInterpolationType = soundParameterInterpolationType;

        this.minPitch = minPitch;
		this.maxPitch = maxPitch;
		this.minVolume = minVolume;
		this.maxVolume = maxVolume;
		this.maxParameterInterpolationSpeed = maxParameterInterpolationSpeed;

		this.canPlaySound = canPlaySound;
		this.canInterpolateParameters = canInterpolateParameters;

		this.looping = true;
		this.delay = 0;
		this.volume = minVolume;

		this.updatePosition();
	}

	@Override
	public void tick() {
        if (this.entity.isRemoved()) {
            this.stop();
			return;
        }

		this.updatePosition();
		float velocity = this.getVelocity();

		if (this.canPlaySound.test(velocity, this.entity)) {
			// In the case that the interpolation type is set to "random", we want different numbers for each parameter.
			float pitchDelta = this.soundParameterInterpolationType.getDeltaValue(this, velocity);
			float volumeDelta = this.soundParameterInterpolationType.getDeltaValue(this, velocity);

			this.pitch = Mth.lerp(pitchDelta, this.minPitch, this.maxPitch);
			this.volume = Mth.lerp(volumeDelta, this.minVolume, this.maxVolume);
		}
		else {
			this.pitch = 0.0F;
			this.volume = 0.0F;
		}
    }

	RandomSource getRandom() {
		return this.random;
	}

	float getMaxParameterInterpolationSpeed() {
		return this.maxParameterInterpolationSpeed;
	}

	private float getVelocity() {
		return (float) this.entity.getKnownSpeed().horizontalDistance();
	}

	boolean canInterpolateParameters() {
		return this.canInterpolateParameters.test(this.getVelocity(), this.entity);
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public boolean canPlaySound() {
		return !this.entity.isSilent();
	}

	private void updatePosition() {
		this.x = (float) this.entity.getX();
		this.y = (float) this.entity.getY();
		this.z = (float) this.entity.getZ();
	}

	public static <T extends Entity> MovingEntitySoundInstanceBuilder<T> builder() {
		return new MovingEntitySoundInstanceBuilder<>();
	}
}
