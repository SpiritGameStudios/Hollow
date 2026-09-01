package dev.spiritstudios.hollow.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public final class MovingEntitySoundInstance<T extends Entity> extends AbstractTickableSoundInstance {
    final T entity;
    private final PitchInterpolationType pitchInterpolationType;
    private final float minPitch;
    private final float maxPitch;
    private final float minVolume;
    private final float maxVolume;

    private final MovingPredicate<T> movingPredicate;

    public MovingEntitySoundInstance(T entity, SoundEvent soundEvent, SoundSource soundSource, PitchInterpolationType pitchInterpolationType, float minPitch, float maxPitch, float minVolume, float maxVolume, MovingPredicate<T> movingPredicate) {
		super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.pitchInterpolationType = pitchInterpolationType;

        this.minPitch = minPitch;
		this.maxPitch = maxPitch;
		this.minVolume = minVolume;
		this.maxVolume = maxVolume;

		this.movingPredicate = movingPredicate;

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
		float velocity = (float) this.entity.getDeltaMovement().horizontalDistance();

		if (this.movingPredicate.test(velocity, this.entity)) {
            this.pitch = this.pitchInterpolationType.getPitch(this, velocity);
            this.volume = Mth.lerp(Mth.clamp(velocity, this.minVolume, this.maxVolume), this.minVolume, this.maxVolume);
        }
		else {
            this.pitch = 0.0F;
            this.volume = 0.0F;
        }
    }

	RandomSource getRandom() {
		return this.random;
	}

	float getMinPitch() {
		return this.minPitch;
	}

	float getMaxPitch() {
		return this.maxPitch;
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
