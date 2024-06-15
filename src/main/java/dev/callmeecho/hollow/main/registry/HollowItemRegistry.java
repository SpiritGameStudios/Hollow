package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.ItemRegistrar;
import net.minecraft.item.Item;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.item.SpawnEggItem;

import static dev.callmeecho.hollow.main.Hollow.GROUP;

@SuppressWarnings("unused")
public class HollowItemRegistry implements ItemRegistrar {
    public static Item FIREFLY_SPAWN_EGG = new SpawnEggItem(HollowEntityTypeRegistry.FIREFLY, 0x102F4E, 0xCAAF94, new Item.Settings().group(GROUP));

    public static Item LOTUS_LILYPAD = new PlaceableOnWaterItem(HollowBlockRegistry.LOTUS_LILYPAD, new Item.Settings().group(GROUP));
}
