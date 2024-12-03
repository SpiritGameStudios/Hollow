package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.worldgen.feature.CattailFeature;
import dev.spiritstudios.hollow.worldgen.feature.FallenTreeFeature;
import dev.spiritstudios.hollow.worldgen.feature.GiantLilypadFeature;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

@SuppressWarnings("unused")
public class HollowFeatures implements MinecraftRegistrar<Feature<?>> {
    @Override
    public Registry<Feature<?>> getRegistry() {
        return Registries.FEATURE;
    }
    
    public static final Feature<FallenTreeFeature.Config> FALLEN_TREE = new FallenTreeFeature();
    public static final Feature<DefaultFeatureConfig> GIANT_LILYPAD = new GiantLilypadFeature();
    public static final Feature<DefaultFeatureConfig> CATTAILS = new CattailFeature();

    @Override
    public Class<Feature<?>> getObjectType() {
        return Registrar.fixGenerics(Feature.class);
    }
}
