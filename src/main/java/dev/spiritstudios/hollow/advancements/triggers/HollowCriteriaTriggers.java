package dev.spiritstudios.hollow.advancements.triggers;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.advancements.triggers.PlayerInteractTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class HollowCriteriaTriggers {
	public static final PlayerInteractTrigger PLAYER_FUELED_ENTITY = register("player_fueled_entity", new PlayerInteractTrigger());

	private static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
		return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Hollow.id(name), criterion);
	}

	public static void init() {
		// NO-OP
	}
}
