package dev.callmeecho.hollow.registry;

import dev.callmeecho.cabinetapi.registry.ItemRegistrar;
import dev.callmeecho.hollow.item.GiantLilyPadItem;
import net.minecraft.item.Item;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static dev.callmeecho.hollow.Hollow.GROUP;
import static dev.callmeecho.hollow.Hollow.MODID;

@SuppressWarnings("unused")
public class HollowItemRegistrar implements ItemRegistrar {
    public static Item FIREFLY_SPAWN_EGG = new SpawnEggItem(HollowEntityTypeRegistrar.FIREFLY, 0x102F4E, 0xCAAF94, new Item.Settings().group(GROUP));

    public static Item LOTUS_LILYPAD = new PlaceableOnWaterItem(HollowBlockRegistrar.LOTUS_LILYPAD, new Item.Settings().group(GROUP));
    public static Item GIANT_LILYPAD = new GiantLilyPadItem(HollowBlockRegistrar.GIANT_LILYPAD, new Item.Settings().group(GROUP));

    public static Item MUSIC_DISC_POSTMORTEM = new Item(new Item.Settings().group(GROUP).maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MODID, "postmortem"))));
}
