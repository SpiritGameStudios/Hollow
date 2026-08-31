package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class HollowItemTags {
    public static final TagKey<Item> CAN_PUT_IN_JAR = create("can_put_in_jar");
	public static final TagKey<Item> FURNACE_BOATS = create("furnace_boats");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Hollow.id(name));
    }
}
