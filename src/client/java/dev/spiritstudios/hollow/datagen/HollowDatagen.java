package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.datagen.tag.BiomeTagProvider;
import dev.spiritstudios.hollow.datagen.tag.BlockTagProvider;
import dev.spiritstudios.hollow.datagen.tag.EntityTypeTagProvider;
import dev.spiritstudios.hollow.worldgen.feature.HollowConfiguredFeatures;
import dev.spiritstudios.hollow.worldgen.feature.HollowPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class HollowDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);

        pack.addProvider(ModelProvider::new);
        pack.addProvider(ClientBlockMetatagProvider::new);

        pack.addProvider(LootTableProvider::new);
        pack.addProvider(RecipeProvider::new);

        pack.addProvider(ConfiguredFeatureProvider::new);
        pack.addProvider(PlacedFeatureProvider::new);

        pack.addProvider(BlockMetatagProvider::new);

        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(BiomeTagProvider::new);
        pack.addProvider(EntityTypeTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder
                .addRegistry(RegistryKeys.CONFIGURED_FEATURE, HollowConfiguredFeatures::bootstrap)
                .addRegistry(RegistryKeys.PLACED_FEATURE, HollowPlacedFeatures::bootstrap);
    }


}
