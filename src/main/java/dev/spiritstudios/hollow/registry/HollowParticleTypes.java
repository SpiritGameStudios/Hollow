package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowParticleTypes implements MinecraftRegistrar<ParticleType<?>> {
    public static final SimpleParticleType FIREFLY_JAR = FabricParticleTypes.simple();

    @Override
    public Registry<ParticleType<?>> getRegistry() {
        return Registries.PARTICLE_TYPE;
    }

    @Override
    public Class<ParticleType<?>> getObjectType() {
        return Registrar.fixGenerics(ParticleType.class);
    }
}
