package dev.spiritstudios.hollow.core.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.item.CopperInstrument;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class HollowRegistries {
	public static final ResourceKey<Registry<CopperInstrument>> COPPER_INSTRUMENT = create("copper_instrument");

	private static <T> ResourceKey<Registry<T>> create(String id) {
		return ResourceKey.createRegistryKey(Hollow.id(id));
	}

	public static void init() {
		DynamicRegistries.registerSynced(COPPER_INSTRUMENT, CopperInstrument.DIRECT_CODEC);
	}
}
