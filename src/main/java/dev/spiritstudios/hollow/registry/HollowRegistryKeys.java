package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.component.CopperInstrument;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class HollowRegistryKeys {
	public static final RegistryKey<Registry<CopperInstrument>> COPPER_INSTRUMENT = of("copper_instrument");

	private static <T> RegistryKey<Registry<T>> of(String id) {
		return RegistryKey.ofRegistry(Hollow.id(id));
	}

	public static void init() {
		DynamicRegistries.registerSynced(COPPER_INSTRUMENT, CopperInstrument.CODEC);
	}
}
