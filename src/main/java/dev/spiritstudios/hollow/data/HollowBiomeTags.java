package dev.spiritstudios.hollow.data;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public final class HollowBiomeTags {
    public static final TagKey<Biome> HAS_CLOSER_FOG = TagKey.of(RegistryKeys.BIOME, Hollow.id("has_closer_fog"));
}
