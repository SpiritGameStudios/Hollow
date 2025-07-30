package dev.spiritstudios.hollow.worldgen.feature;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ClampedIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public final class HollowPlacedFeatures {
    public static final RegistryKey<PlacedFeature> CATTAILS = of("cattails");

    public static final RegistryKey<PlacedFeature> PATCH_TWIG = of("patch_twig");
    public static final RegistryKey<PlacedFeature> PATCH_CAMPION = of("patch_campion");
    public static final RegistryKey<PlacedFeature> PATCH_GIANT_LILYPAD = of("patch_giant_lilypad");
    public static final RegistryKey<PlacedFeature> PATCH_GRASS_BIRCH = of("patch_grass_birch");
    public static final RegistryKey<PlacedFeature> PATCH_TALL_GRASS_BIRCH = of("patch_tall_grass_birch");

    public static final RegistryKey<PlacedFeature> FALLEN_BIRCH = of("fallen_birch");
    public static final RegistryKey<PlacedFeature> FALLEN_OAK = of("fallen_oak");

    public static final RegistryKey<PlacedFeature> HUGE_BROWN_MUSHROOM_SWAMP = of("huge_brown_mushroom_swamp");
    public static final RegistryKey<PlacedFeature> HUGE_RED_MUSHROOM_SWAMP = of("huge_red_mushroom_swamp");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        PlacedFeatureHelper helper = new PlacedFeatureHelper(featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE), featureRegisterable);

        helper.add(
                FALLEN_BIRCH,
                HollowConfiguredFeatures.FALLEN_BIRCH,
                SquarePlacementModifier.of(),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4)
        );

        helper.add(
                FALLEN_OAK,
                HollowConfiguredFeatures.FALLEN_OAK,
                SquarePlacementModifier.of(),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                BiomePlacementModifier.of(),
                RarityFilterPlacementModifier.of(4)
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
                PATCH_CAMPION,
                HollowConfiguredFeatures.PATCH_CAMPION,
                RarityFilterPlacementModifier.of(7),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                CountPlacementModifier.of(ClampedIntProvider.create(UniformIntProvider.create(-3, 1), 0, 1)),
                BiomePlacementModifier.of()
        );

        helper.add(
                PATCH_GRASS_BIRCH,
                VegetationConfiguredFeatures.PATCH_GRASS,
                CountPlacementModifier.of(20),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                PATCH_TALL_GRASS_BIRCH,
                VegetationConfiguredFeatures.PATCH_TALL_GRASS,
                CountPlacementModifier.of(1),
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                HUGE_BROWN_MUSHROOM_SWAMP,
                TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM,
                CountPlacementModifier.of(1),
                RarityFilterPlacementModifier.of(2),
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                HUGE_RED_MUSHROOM_SWAMP,
                TreeConfiguredFeatures.HUGE_RED_MUSHROOM,
                CountPlacementModifier.of(1),
                RarityFilterPlacementModifier.of(2),
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of()
        );

        helper.add(
                PATCH_GIANT_LILYPAD,
                HollowConfiguredFeatures.PATCH_GIANT_LILYPAD,
                CountPlacementModifier.of(1),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
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