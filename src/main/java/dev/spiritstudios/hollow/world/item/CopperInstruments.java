package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public final class CopperInstruments {
	public static final ResourceKey<CopperInstrument> GREAT_SKY_FALLING = create("great_sky_falling");
	public static final ResourceKey<CopperInstrument> OLD_HYMN_RESTING = create("old_hymn_resting");
	public static final ResourceKey<CopperInstrument> PURE_WATER_DESIRE = create("pure_water_desire");
	public static final ResourceKey<CopperInstrument> HUMBLE_FIRE_MEMORY = create("humble_fire_memory");
	public static final ResourceKey<CopperInstrument> DRY_URGE_ANGER = create("dry_urge_anger");
	public static final ResourceKey<CopperInstrument> CLEAR_TEMPER_JOURNEY = create("clear_temper_journey");
	public static final ResourceKey<CopperInstrument> FRESH_NEST_THOUGHT = create("fresh_nest_thought");
	public static final ResourceKey<CopperInstrument> SECRET_LAKE_TEAR = create("secret_lake_tear");
	public static final ResourceKey<CopperInstrument> FEARLESS_RIVER_GIFT = create("fearless_river_gift");
	public static final ResourceKey<CopperInstrument> SWEET_MOON_LOVE = create("sweet_moon_love");

	public static void bootstrap(BootstrapContext<CopperInstrument> context) {
		register(context, GREAT_SKY_FALLING, "great", "sky", "falling", 4.0F, 256F);
		register(context, OLD_HYMN_RESTING, "old", "hymn", "resting", 4.0F, 256F);
		register(context, PURE_WATER_DESIRE, "pure", "water", "desire", 4.0F, 256F);
		register(context, HUMBLE_FIRE_MEMORY, "humble", "fire", "memory", 4.0F, 256F);
		register(context, DRY_URGE_ANGER, "dry", "urge", "anger", 4.0F, 256F);
		register(context, CLEAR_TEMPER_JOURNEY, "clear", "temper", "journey", 4.0F, 256F);
		register(context, FRESH_NEST_THOUGHT, "fresh", "nest", "thought", 4.0F, 256F);
		register(context, SECRET_LAKE_TEAR, "secret", "lake", "tear", 4.0F, 256F);
		register(context, FEARLESS_RIVER_GIFT, "fearless", "river", "gift", 4.0F, 256F);
		register(context, SWEET_MOON_LOVE, "sweet", "moon", "love", 4.0F, 256F);
	}

	private static void register(BootstrapContext<CopperInstrument> registry, ResourceKey<CopperInstrument> key, String call, String melody, String bass, float useDuration, float range) {
		registry.register(key, CopperInstrument.of(Hollow.MODID, call, melody, bass, useDuration, range));
	}

	private static ResourceKey<CopperInstrument> create(String id) {
		return ResourceKey.create(HollowRegistries.COPPER_INSTRUMENT, Hollow.id(id));
	}
}
