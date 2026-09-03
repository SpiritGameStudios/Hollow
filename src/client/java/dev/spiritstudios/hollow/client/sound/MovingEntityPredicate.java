package dev.spiritstudios.hollow.client.sound;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface MovingEntityPredicate<T extends Entity> {
	boolean test(float velocity, T entity);
}
