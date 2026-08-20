package dev.spiritstudios.hollow.client.data.gen;

import dev.spiritstudios.hollow.data.gen.*;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.hollow.world.entity.HollowDamageTypes;
import dev.spiritstudios.hollow.world.item.CopperInstruments;
import dev.spiritstudios.hollow.world.item.HollowJukeboxSongs;
import dev.spiritstudios.hollow.world.level.gen.feature.HollowConfiguredFeatures;
import dev.spiritstudios.hollow.world.level.gen.feature.HollowPlacements;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class HollowDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(HollowModelProvider::new);
        pack.addProvider(HollowSoundsProvider::new);

        pack.addProvider(HollowLootTableProvider::new);
        pack.addProvider(HollowRecipeProvider::new);

        pack.addProvider(HollowConfiguredFeatureProvider::new);
        pack.addProvider(HollowPlacedFeatureProvider::new);

        pack.addProvider(HollowDamageTypeProvider::new);
        pack.addProvider(HollowCopperInstrumentProvider::new);
        pack.addProvider(HollowJukeboxSongProvider::new);

        HollowTagProviders.addAll(pack);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder
                .add(Registries.CONFIGURED_FEATURE, HollowConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, HollowPlacements::bootstrap)
                .add(Registries.DAMAGE_TYPE, HollowDamageTypes::bootstrap)
                .add(Registries.JUKEBOX_SONG, HollowJukeboxSongs::bootstrap)
                .add(HollowRegistryKeys.COPPER_INSTRUMENT, CopperInstruments::bootstrap);
    }
}
