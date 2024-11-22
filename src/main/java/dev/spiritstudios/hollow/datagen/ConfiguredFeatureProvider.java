package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.hollow.registry.HollowFeatureRegistrar;
import dev.spiritstudios.hollow.worldgen.feature.FallenTreeFeatureConfig;
import dev.spiritstudios.hollow.worldgen.feature.HollowConfiguredFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public ConfiguredFeatureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.add(HollowConfiguredFeatures.FALLEN_OAK, fallenTree(HollowBlockRegistrar.OAK_HOLLOW_LOG, false, true));
        entries.add(HollowConfiguredFeatures.FALLEN_BIRCH, fallenTree(HollowBlockRegistrar.BIRCH_HOLLOW_LOG, true, true));

        entries.add(HollowConfiguredFeatures.PATCH_TWIG, createRandomPatch(HollowBlockRegistrar.TWIG, 10));
        entries.add(HollowConfiguredFeatures.PATCH_CAMPION, createRandomPatch(HollowBlockRegistrar.CAMPION, 10));
        entries.add(
                HollowConfiguredFeatures.PATCH_GIANT_LILYPAD,
                createRandomPatch(
                        PlacedFeatures.createEntry(HollowFeatureRegistrar.GIANT_LILYPAD, new DefaultFeatureConfig()),
                        10
                )
        );
    }

    @Override
    public String getName() {
        return "Hollow/Configured Features";
    }

    public static ConfiguredFeature<?, ?> fallenTree(Block block, boolean polypore, boolean mossy) {
        return new ConfiguredFeature<>(
                HollowFeatureRegistrar.FALLEN_TREE,
                new FallenTreeFeatureConfig(BlockStateProvider.of(block), polypore, mossy)
        );
    }

    public static ConfiguredFeature<?, ?> createRandomPatch(RegistryEntry<PlacedFeature> feature, int tries) {
        return new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(
                        tries,
                        feature
                )
        );
    }

    public static ConfiguredFeature<?, ?> createRandomPatch(BlockStateProvider block, int tries) {
        return createRandomPatch(
                PlacedFeatures.createEntry(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(block)
                ),
                tries
        );

    }

    private static ConfiguredFeature<?, ?> createRandomPatch(Block block, int tries) {
        return createRandomPatch(BlockStateProvider.of(block), tries);
    }
}
