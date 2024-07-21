package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.hollow.main.Hollow;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class HollowTags {
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(Registries.BLOCK.getKey(), id("hollow_logs"));

    public static final TagKey<Biome> CLOSER_FOG = TagKey.of(RegistryKeys.BIOME, id("closer_fog"));


    public static Identifier id(String path) {
        return Identifier.of(Hollow.MODID, path);
    }
}
