package dev.spiritstudios.hollow;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class HollowTags {
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, Hollow.id("hollow_logs"));

    public static final TagKey<Biome> CLOSER_FOG = TagKey.of(RegistryKeys.BIOME, Hollow.id("closer_fog"));
}
