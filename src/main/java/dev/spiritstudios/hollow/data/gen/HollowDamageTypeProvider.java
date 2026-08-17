package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class HollowDamageTypeProvider extends FabricDynamicRegistryProvider {
    public HollowDamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
        HolderLookup<DamageType> lookup = wrapperLookup.lookupOrThrow(Registries.DAMAGE_TYPE);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));
    }

    @Override
    public String getName() {
        return "Damage Types";
    }
}
