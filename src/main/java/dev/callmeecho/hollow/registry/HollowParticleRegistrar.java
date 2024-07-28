package dev.callmeecho.hollow.registry;

import dev.callmeecho.cabinetapi.registry.ParticleTypeRegistrar;
import dev.callmeecho.cabinetapi.registry.Registrar;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowParticleRegistrar implements ParticleTypeRegistrar {
    public static final SimpleParticleType FIREFLY_JAR = FabricParticleTypes.simple();
}
