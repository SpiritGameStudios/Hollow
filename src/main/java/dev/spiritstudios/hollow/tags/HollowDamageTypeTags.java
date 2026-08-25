package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class HollowDamageTypeTags {
    public static final TagKey<DamageType> IS_SCULK_JAW = create("is_sculk_jaw");

	private static TagKey<DamageType> create(String name) {
		return TagKey.create(Registries.DAMAGE_TYPE, Hollow.id(name));
	}
}
