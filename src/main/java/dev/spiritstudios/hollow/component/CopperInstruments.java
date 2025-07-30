package dev.spiritstudios.hollow.component;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;

public final class CopperInstruments {
	public static final RegistryKey<CopperInstrument> GREAT_SKY_FALLING = ofKey("great_sky_falling");
	public static final RegistryKey<CopperInstrument> OLD_HYMN_RESTING = ofKey("old_hymn_resting");
	public static final RegistryKey<CopperInstrument> PURE_WATER_DESIRE = ofKey("pure_water_desire");
	public static final RegistryKey<CopperInstrument> HUMBLE_FIRE_MEMORY = ofKey("humble_fire_memory");
	public static final RegistryKey<CopperInstrument> DRY_URGE_ANGER = ofKey("dry_urge_anger");
	public static final RegistryKey<CopperInstrument> CLEAR_TEMPER_JOURNEY = ofKey("clear_temper_journey");
	public static final RegistryKey<CopperInstrument> FRESH_NEST_THOUGHT = ofKey("fresh_nest_thought");
	public static final RegistryKey<CopperInstrument> SECRET_LAKE_TEAR = ofKey("secret_lake_tear");
	public static final RegistryKey<CopperInstrument> FEARLESS_RIVER_GIFT = ofKey("fearless_river_gift");
	public static final RegistryKey<CopperInstrument> SWEET_MOON_LOVE = ofKey("sweet_moon_love");

	public static void bootstrap(Registerable<CopperInstrument> registerable) {
		register(registerable, GREAT_SKY_FALLING, "great", "sky", "falling", 4.0F, 256F);
		register(registerable, OLD_HYMN_RESTING, "old", "hymn", "resting", 4.0F, 256F);
		register(registerable, PURE_WATER_DESIRE, "pure", "water", "desire", 4.0F, 256F);
		register(registerable, HUMBLE_FIRE_MEMORY, "humble", "fire", "memory", 4.0F, 256F);
		register(registerable, DRY_URGE_ANGER, "dry", "urge", "anger", 4.0F, 256F);
		register(registerable, CLEAR_TEMPER_JOURNEY, "clear", "temper", "journey", 4.0F, 256F);
		register(registerable, FRESH_NEST_THOUGHT, "fresh", "nest", "thought", 4.0F, 256F);
		register(registerable, SECRET_LAKE_TEAR, "secret", "lake", "tear", 4.0F, 256F);
		register(registerable, FEARLESS_RIVER_GIFT, "fearless", "river", "gift", 4.0F, 256F);
		register(registerable, SWEET_MOON_LOVE, "sweet", "moon", "love", 4.0F, 256F);
	}

	private static void register(Registerable<CopperInstrument> registry, RegistryKey<CopperInstrument> key, String call, String melody, String bass, float useDuration, float range) {
		registry.register(key, CopperInstrument.of(Hollow.MODID, call, melody, bass, useDuration, range));
	}

	private static RegistryKey<CopperInstrument> ofKey(String id) {
		return RegistryKey.of(HollowRegistryKeys.COPPER_INSTRUMENT, Hollow.id(id));
	}
}
