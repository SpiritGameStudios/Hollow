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

	public static final ResourceKey<Item> ACACIA_FURNACE_BOAT = create("acacia_furnace_boat");
	public static final ResourceKey<Item> BAMBOO_FURNACE_RAFT = create("bamboo_furnace_raft");
	public static final ResourceKey<Item> BIRCH_FURNACE_BOAT = create("birch_furnace_boat");
	public static final ResourceKey<Item> CHERRY_FURNACE_BOAT = create("cherry_furnace_boat");
	public static final ResourceKey<Item> DARK_OAK_FURNACE_BOAT = create("dark_oak_furnace_boat");
	public static final ResourceKey<Item> JUNGLE_FURNACE_BOAT = create("jungle_furnace_boat");
	public static final ResourceKey<Item> MANGROVE_FURNACE_BOAT = create("mangrove_furnace_boat");
	public static final ResourceKey<Item> OAK_FURNACE_BOAT = create("oak_furnace_boat");
	public static final ResourceKey<Item> PALE_OAK_FURNACE_BOAT = create("pale_oak_furnace_boat");
	public static final ResourceKey<Item> SPRUCE_FURNACE_BOAT = create("spruce_furnace_boat");

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
