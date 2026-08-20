package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HollowPlacedFeatureProvider extends FabricDynamicRegistryProvider {
    public HollowPlacedFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        HolderLookup<PlacedFeature> lookup = registries.lookupOrThrow(Registries.PLACED_FEATURE);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));

        // region Replacements
        PlacedFeatureDatagenHelper helper = new PlacedFeatureDatagenHelper(
                registries.lookupOrThrow(Registries.CONFIGURED_FEATURE),
                entries
        );

        helper.add(
                VegetationPlacements.TREES_BIRCH,
                TreeFeatures.BIRCH_BEES_0002,
                CountPlacement.of(new WeightedListInt(WeightedList.<IntProvider>builder()
                        .add(ConstantInt.of(9), 9)
                        .add(ConstantInt.of(8), 1)
                        .build())),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );

        helper.add(
                VegetationPlacements.BIRCH_TALL,
                VegetationFeatures.BIRCH_TALL,
                CountPlacement.of(new WeightedListInt(WeightedList.<IntProvider>builder()
                        .add(ConstantInt.of(9), 9)
                        .add(ConstantInt.of(8), 1)
                        .build())),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome(),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        // endregion
    }

    private record PlacedFeatureDatagenHelper(HolderGetter<ConfiguredFeature<?, ?>> lookup,
                                              Entries entries) {
        public void add(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
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
        return "Placed Features";
    }
}
