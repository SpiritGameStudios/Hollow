package dev.spiritstudios.hollow;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public final class HollowTags {
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, Hollow.id("hollow_logs"));

    public static final TagKey<Biome> CLOSER_FOG = TagKey.of(RegistryKeys.BIOME, Hollow.id("closer_fog"));

    public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("immune_to_sculk_jaw"));

    public static final TagKey<EntityType<?>> POISONS_FROG = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("poisons_frog"));
}
