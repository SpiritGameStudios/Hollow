package dev.spiritstudios.hollow.worldgen.feature;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public final class HollowPlacedFeatures {
    public static final RegistryKey<PlacedFeature> BIRCH_BEES_0002 = of("birch_bees_0002");
    public static final RegistryKey<PlacedFeature> SUPER_BIRCH_BEES_0002 = of("super_birch_bees_0002");

    public static final RegistryKey<PlacedFeature> BIRCH_TALL = of("birch_tall");

    public static final RegistryKey<PlacedFeature> CATTAILS = of("cattails");

    public static final RegistryKey<PlacedFeature> PATCH_TWIG = of("patch_twig");
    public static final RegistryKey<PlacedFeature> PATCH_WATERLILY = of("patch_waterlily");
    public static final RegistryKey<PlacedFeature> PATCH_CAMPION = of("patch_campion");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        PlacedFeatureHelper helper = new PlacedFeatureHelper(featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE), featureRegisterable);

        helper.add(
                BIRCH_BEES_0002,
                HollowConfiguredFeatures.BIRCH_BEES_0002,
                PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING)
        );

        helper.add(
                SUPER_BIRCH_BEES_0002,
                HollowConfiguredFeatures.SUPER_BIRCH_BEES_0002,
                PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING)
        );

        helper.add(
                BIRCH_TALL,
                HollowConfiguredFeatures.BIRCH_TALL,
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                CountPlacementModifier.of(new WeightedListIntProvider(DataPool.<IntProvider>builder()
                        .add(ConstantIntProvider.create(10), 9)
                        .add(ConstantIntProvider.create(11), 1)
                        .build())),
                SquarePlacementModifier.of(),
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                BiomePlacementModifier.of(),
                PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING)
        );

        helper.add(
                CATTAILS,
                HollowConfiguredFeatures.CATTAILS,
                CountPlacementModifier.of(2),
                RarityFilterPlacementModifier.of(2),
                PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                PATCH_TWIG,
                HollowConfiguredFeatures.PATCH_TWIG,
                CountPlacementModifier.of(2),
                RarityFilterPlacementModifier.of(2),
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                PATCH_WATERLILY,
                HollowConfiguredFeatures.PATCH_WATERLILY,
                CountPlacementModifier.of(4),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                PATCH_CAMPION,
                HollowConfiguredFeatures.PATCH_CAMPION,
                CountPlacementModifier.of(1),
                RarityFilterPlacementModifier.of(2),
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );
    }

    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Hollow.MODID, id));
    }


    private record PlacedFeatureHelper(RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup,
                                       Registerable<PlacedFeature> featureRegisterable) {
        public void add(RegistryKey<PlacedFeature> key, RegistryKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
            featureRegisterable.register(
                    key,
                    new PlacedFeature(
                            lookup.getOrThrow(configuredKey),
                            List.of(modifiers)
                    )
            );
        }
    }
}