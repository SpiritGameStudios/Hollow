package dev.callmeecho.hollow.main.registry;

import com.mojang.serialization.Codec;
import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.main.particle.FireflyJarParticle;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowParticleRegistrar implements Registrar<ParticleType<?>> {
    @Override
    public Registry<ParticleType<?>> getRegistry() {
        return Registries.PARTICLE_TYPE;
    }
    
    public static final DefaultParticleType FIREFLY_JAR = FabricParticleTypes.simple();
}
