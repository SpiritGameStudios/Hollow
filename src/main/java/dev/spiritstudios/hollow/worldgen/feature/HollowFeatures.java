package dev.spiritstudios.hollow.worldgen.feature;

import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

@SuppressWarnings("unused")
public final class HollowFeatures {
    public static final Feature<FallenTreeFeature.Config> FALLEN_TREE = new FallenTreeFeature();
    public static final Feature<DefaultFeatureConfig> GIANT_LILYPAD = new GiantLilypadFeature();
    public static final Feature<DefaultFeatureConfig> CATTAILS = new CattailFeature();
}
