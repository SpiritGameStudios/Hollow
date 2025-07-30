package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class HollowJukeboxSongProvider extends FabricDynamicRegistryProvider {
    public HollowJukeboxSongProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
        RegistryWrapper<JukeboxSong> lookup = wrapperLookup.getOrThrow(RegistryKeys.JUKEBOX_SONG);

        lookup.streamKeys()
                .filter(key ->
                        key.getValue().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));
    }

    @Override
    public String getName() {
        return "Jukebox Songs";
    }
}
