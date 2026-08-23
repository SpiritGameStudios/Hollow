package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.item.HollowJukeboxSongs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;

public final class HollowItemIds {
    public static final ResourceKey<Item> COPPER_HORN = create("copper_horn");
    public static final ResourceKey<Item> MUSIC_DISC_POSTMORTEM = createMusicDisc(HollowJukeboxSongs.POSTMORTEM);
	public static final ResourceKey<Item> MUSIC_DISC_ONLY_YOU = createMusicDisc(HollowJukeboxSongs.ONLY_YOU);

    private static ResourceKey<Item> create(final String name) {
        return ResourceKey.create(Registries.ITEM, Hollow.id(name));
    }

    private static ResourceKey<Item> createMusicDisc(final ResourceKey<JukeboxSong> music) {
        return music.dependent(Registries.ITEM, path -> "music_disc_" + path);
    }

    private static ResourceKey<Item> createSpawnEgg(final ResourceKey<EntityType<?>> entity) {
        return entity.dependent(Registries.ITEM, "_spawn_egg");
    }
}
