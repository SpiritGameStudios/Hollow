package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.item.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class HollowCopperInstrumentProvider extends FabricDynamicRegistryProvider {
    public HollowCopperInstrumentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
        HolderLookup<CopperInstrument> lookup = wrapperLookup.lookupOrThrow(HollowRegistryKeys.COPPER_INSTRUMENT);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));
    }

    @Override
    public String getName() {
        return "Copper Instruments";
    }
}
