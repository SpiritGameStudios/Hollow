package dev.spiritstudios.hollow.advancements.triggers;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.advancements.triggers.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class HollowCriteriaTriggers {
	public static final PlayerTrigger PLAYER_PROPEL_FURNACE_BOAT = register("player_propel_furnace_boat", new PlayerTrigger());
	public static final PlayerTrigger PLAYER_INSERT_JAR_IN_JAR = register("player_insert_jar_in_jar", new PlayerTrigger());

	private static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
		return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Hollow.id(name), criterion);
	}

	public static void init() {
		// NO-OP
	}
}
