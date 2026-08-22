package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class HollowEntityTypeTags {
	public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW = create("immune_to_sculk_jaw");

	private static TagKey<EntityType<?>> create(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, Hollow.id(name));
	}
}
