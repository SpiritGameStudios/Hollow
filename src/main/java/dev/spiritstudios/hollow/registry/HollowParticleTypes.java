package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class HollowParticleTypes {
    public static final SimpleParticleType SCREAM = register("scream", true);
    public static final SimpleParticleType JAR_FIREFLY = register("jar_firefly", false);

    private static SimpleParticleType register(String name, boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Hollow.id(name), FabricParticleTypes.simple(overrideLimiter));
    }

    public static void init() {
        // NO-OP
    }
}
