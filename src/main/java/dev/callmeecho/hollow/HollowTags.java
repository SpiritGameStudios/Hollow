package dev.callmeecho.hollow;

import dev.callmeecho.hollow.main.Hollow;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class HollowTags {
    public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(Registries.BLOCK.getKey(), id("hollow_logs"));

    public static Identifier id(String path) {
        return new Identifier(Hollow.MODID, path);
    }
}
