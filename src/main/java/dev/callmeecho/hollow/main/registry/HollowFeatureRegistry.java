package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.main.worldgen.CattailFeature;
import dev.callmeecho.hollow.main.worldgen.FallenTreeFeature;
import dev.callmeecho.hollow.main.worldgen.FallenTreeFeatureConfig;
import dev.callmeecho.hollow.main.worldgen.GiantLilypadFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

@SuppressWarnings("unused")
public class HollowFeatureRegistry implements Registrar<Feature<?>> {
    @Override
    public Registry<Feature<?>> getRegistry() {
        return Registries.FEATURE;
    }
    
    public static final Feature<FallenTreeFeatureConfig> FALLEN_TREE = new FallenTreeFeature(FallenTreeFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> GIANT_LILYPAD = new GiantLilypadFeature(DefaultFeatureConfig.CODEC);
    public static final Feature<DefaultFeatureConfig> CATTAILS = new CattailFeature(DefaultFeatureConfig.CODEC);
}
