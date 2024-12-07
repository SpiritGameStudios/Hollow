package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class HollowDamageTypes {
    public static final RegistryKey<DamageType> SCULK_JAW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Hollow.id("sculk_jaw"));
}
