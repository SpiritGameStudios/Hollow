package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class HollowJukeboxSongProvider extends FabricDynamicRegistryProvider {
    public HollowJukeboxSongProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider wrapperLookup, Entries entries) {
        HolderLookup<JukeboxSong> lookup = wrapperLookup.lookupOrThrow(Registries.JUKEBOX_SONG);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));
    }

    @Override
    public String getName() {
        return "Jukebox Songs";
    }
}
