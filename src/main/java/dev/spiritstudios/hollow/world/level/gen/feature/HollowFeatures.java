package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

@SuppressWarnings("unused")
public final class HollowFeatures {
    public static final Feature<FallenTreeFeature.Config> FALLEN_TREE = register("fallen_tree", new FallenTreeFeature());
    public static final Feature<NoneFeatureConfiguration> GIANT_LILYPAD = register("giant_lilypad", new GiantLilypadFeature());
    public static final Feature<NoneFeatureConfiguration> CATTAILS = register("cattails", new CattailFeature());

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Hollow.id(name), feature);
    }

    public static void init() {
        // NO-OP
    }
}
