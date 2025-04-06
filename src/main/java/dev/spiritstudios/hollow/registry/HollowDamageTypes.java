package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class HollowDamageTypes {
    public static final RegistryKey<DamageType> SCULK_JAW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Hollow.id("sculk_jaw"));
    public static final RegistryKey<DamageType> DIRE_CURSE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Hollow.id("dire_curse"));

    public static void bootstrap(Registerable<DamageType> registerable) {
        registerable.register(
                SCULK_JAW,
                new DamageType(
                        "sculk_jaw",
                        DamageScaling.ALWAYS,
                        0.1F
                )
        );

        registerable.register(
                DIRE_CURSE,
                new DamageType(
                        "dire_curse",
                        DamageScaling.ALWAYS,
                        0.1F
                )
        );
    }
}
