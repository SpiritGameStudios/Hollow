package dev.spiritstudios.hollow.item;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.HollowBlocks;
import dev.spiritstudios.hollow.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.component.CopperInstruments;
import dev.spiritstudios.hollow.component.HollowDataComponentTypes;
import dev.spiritstudios.hollow.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.sound.HollowJukeboxSongs;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class HollowItems {
	public static final Item FIREFLY_SPAWN_EGG = register(
			"firefly_spawn_egg",
			settings -> new SpawnEggItem(HollowEntityTypes.FIREFLY, settings)
	);

	public static final Item LOTUS_LILYPAD = register(
			"lotus_lilypad",
			settings -> new PlaceableOnWaterItem(HollowBlocks.LOTUS_LILYPAD, settings),
			new Item.Settings().useBlockPrefixedTranslationKey()
	);

	public static final Item GIANT_LILYPAD = register(
			"giant_lilypad",
			settings -> new PlaceableOnWaterItem(HollowBlocks.GIANT_LILYPAD, settings),
			new Item.Settings().useBlockPrefixedTranslationKey()
	);

	public static final Item MUSIC_DISC_POSTMORTEM = register(
			"music_disc_postmortem",
			new Item.Settings()
					.maxCount(1)
					.rarity(Rarity.RARE)
					.jukeboxPlayable(HollowJukeboxSongs.POSTMORTEM)
	);

	public static final Item COPPER_HORN = register(
			"copper_horn",
			CopperHornItem::new,
			new Item.Settings()
					.maxCount(1)
					.rarity(Rarity.UNCOMMON)
					.component(
							HollowDataComponentTypes.COPPER_INSTRUMENT,
							new CopperInstrumentComponent(CopperInstruments.GREAT_SKY_FALLING)
					)
	);

	private static RegistryKey<Item> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.ITEM, Hollow.id(id));
	}

	public static Item register(String id, Function<Item.Settings, Item> factory) {
		return register(keyOf(id), factory, new Item.Settings());
	}

	public static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
		return register(keyOf(id), factory, settings);
	}

	public static Item register(String id, Item.Settings settings) {
		return register(keyOf(id), Item::new, settings);
	}

	public static Item register(String id) {
		return register(keyOf(id), Item::new, new Item.Settings());
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory) {
		return register(key, factory, new Item.Settings());
	}

	public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
		Item item = factory.apply(settings.registryKey(key));
		if (item instanceof BlockItem blockItem) {
			blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
		}

		return Registry.register(Registries.ITEM, key, item);
	}

	public static void init() {
		// NO-OP
	}
}
