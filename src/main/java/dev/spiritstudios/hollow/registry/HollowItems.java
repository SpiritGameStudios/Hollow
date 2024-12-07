package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.item.CopperHornItem;
import dev.spiritstudios.hollow.item.GiantLilyPadItem;
import net.minecraft.item.Item;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

@SuppressWarnings("unused")
public final class HollowItems {
    public static final Item FIREFLY_SPAWN_EGG = new SpawnEggItem(
            HollowEntityTypes.FIREFLY,
            0x102F4E, 0xCAAF94,
            new Item.Settings()
    );

    public static final Item LOTUS_LILYPAD = new PlaceableOnWaterItem(HollowBlocks.LOTUS_LILYPAD, new Item.Settings());
    public static final Item GIANT_LILYPAD = new GiantLilyPadItem(HollowBlocks.GIANT_LILYPAD, new Item.Settings());

    public static final Item MUSIC_DISC_POSTMORTEM = new Item(
            new Item.Settings()
                    .maxCount(1)
                    .rarity(Rarity.RARE)
                    .jukeboxPlayable(RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Hollow.id("postmortem")))
    );

    public static final Item COPPER_HORN = new CopperHornItem(new Item.Settings().maxCount(1));
}
