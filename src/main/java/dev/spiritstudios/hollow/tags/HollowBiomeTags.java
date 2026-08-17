package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class HollowBiomeTags {
    public static final TagKey<Biome> HAS_CLOSER_FOG = TagKey.create(Registries.BIOME, Hollow.id("has_closer_fog"));
}
