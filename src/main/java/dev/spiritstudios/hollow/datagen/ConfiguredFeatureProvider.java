package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public ConfiguredFeatureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<ConfiguredFeature<?, ?>> lookup = registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE);

        lookup.streamKeys()
                .filter(key ->
                        key.getValue().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));
    }

    @Override
    public String getName() {
        return "Hollow/Configured Features";
    }
}
