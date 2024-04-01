package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.item.CabinetItemSettings;
import dev.callmeecho.cabinetapi.registry.ItemRegistrar;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;

import static dev.callmeecho.hollow.main.Hollow.GROUP;
import static dev.callmeecho.hollow.main.registry.HallowSoundEventRegistry.PRENUPTIAL;

@SuppressWarnings("unused")
public class HollowItemRegistry implements ItemRegistrar {
    public static Item FIREFLY_SPAWN_EGG = new SpawnEggItem(HollowEntityTypeRegistry.FIREFLY, 0x102F4E, 0xCAAF94, new CabinetItemSettings().group(GROUP));

    public static Item LOTUS_LILYPAD = new PlaceableOnWaterItem(HollowBlockRegistry.LOTUS_LILYPAD, new CabinetItemSettings().group(GROUP));
    public static Item MUSIC_DISC_PRENUPTIAL = new MusicDiscItem(2, PRENUPTIAL, new CabinetItemSettings().maxCount(1).rarity(Rarity.EPIC).group(GROUP), 183);
}
