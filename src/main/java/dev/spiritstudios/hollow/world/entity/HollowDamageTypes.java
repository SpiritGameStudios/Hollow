package dev.spiritstudios.hollow.world.entity;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public final class HollowDamageTypes {
    public static final ResourceKey<DamageType> SCULK_JAW = ResourceKey.create(Registries.DAMAGE_TYPE, Hollow.id("sculk_jaw"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(
                SCULK_JAW,
                new DamageType(
                        "sculk_jaw",
                        DamageScaling.ALWAYS,
                        0.1F
                )
        );
    }
}
