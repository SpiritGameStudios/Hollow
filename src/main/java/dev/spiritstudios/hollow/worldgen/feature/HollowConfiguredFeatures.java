package dev.spiritstudios.hollow.worldgen.feature;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public final class HollowConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_OAK = of("fallen_oak");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH = of("fallen_birch");

    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_TWIG = of("patch_twig");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_CAMPION = of("patch_campion");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_GIANT_LILYPAD = of("patch_giant_lilypad");

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Hollow.MODID, id));
    }
}