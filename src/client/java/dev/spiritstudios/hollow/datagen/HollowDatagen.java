package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.component.CopperInstruments;
import dev.spiritstudios.hollow.entity.HollowDamageTypes;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.hollow.sound.HollowJukeboxSongs;
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

        pack.addProvider(HollowModelProvider::new);
        pack.addProvider(HollowSoundsProvider::new);
        pack.addProvider(HollowClientBlockMetatagProvider::new);

        pack.addProvider(LootTableProvider::new);
        pack.addProvider(HollowRecipeProvider::new);

        pack.addProvider(ConfiguredFeatureProvider::new);
        pack.addProvider(PlacedFeatureProvider::new);

        pack.addProvider(HollowDamageTypeProvider::new);
        pack.addProvider(HollowCopperInstrumentProvider::new);
        pack.addProvider(HollowJukeboxSongProvider::new);

        MetatagProviders.addAll(pack);
        TagProviders.addAll(pack);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder
                .addRegistry(RegistryKeys.CONFIGURED_FEATURE, HollowConfiguredFeatures::bootstrap)
                .addRegistry(RegistryKeys.PLACED_FEATURE, HollowPlacedFeatures::bootstrap)
                .addRegistry(RegistryKeys.DAMAGE_TYPE, HollowDamageTypes::bootstrap)
                .addRegistry(RegistryKeys.JUKEBOX_SONG, HollowJukeboxSongs::bootstrap)
                .addRegistry(HollowRegistryKeys.COPPER_INSTRUMENT, CopperInstruments::bootstrap);
    }
}
