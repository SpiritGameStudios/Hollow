package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.worldgen.feature.HollowConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlacedFeatureProvider extends FabricDynamicRegistryProvider {
    public PlacedFeatureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<PlacedFeature> lookup = registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE);

        lookup.streamKeys()
                .filter(key ->
                        key.getValue().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));

        // region Replacements
        PlacedFeatureDatagenHelper helper = new PlacedFeatureDatagenHelper(
                registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE),
                entries
        );

        helper.add(
                VegetationPlacedFeatures.TREES_BIRCH,
                TreeConfiguredFeatures.BIRCH_BEES_0002,
                CountPlacementModifier.of(new WeightedListIntProvider(DataPool.<IntProvider>builder()
                        .add(ConstantIntProvider.create(9), 9)
                        .add(ConstantIntProvider.create(8), 1)
                        .build())),
                HeightmapPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of(),
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING)
        );

        helper.add(
                VegetationPlacedFeatures.BIRCH_TALL,
                VegetationConfiguredFeatures.BIRCH_TALL,
                CountPlacementModifier.of(new WeightedListIntProvider(DataPool.<IntProvider>builder()
                        .add(ConstantIntProvider.create(9), 9)
                        .add(ConstantIntProvider.create(8), 1)
                        .build())),
                HeightmapPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR),
                BiomePlacementModifier.of(),
                SquarePlacementModifier.of(),
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                PlacedFeatures.wouldSurvive(Blocks.BIRCH_SAPLING)
        );

        // endregion
    }

    private record PlacedFeatureDatagenHelper(RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup,
                                              Entries entries) {
        public void add(RegistryKey<PlacedFeature> key, RegistryKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
            entries.add(
                    key,
                    new PlacedFeature(
                            lookup.getOrThrow(configuredKey),
                            List.of(modifiers)
                    )
            );
        }
    }

    @Override
    public String getName() {
        return "Hollow/Placed Features";
    }
}
