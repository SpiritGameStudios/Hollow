package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class HollowBlockTags {
    public static final TagKey<Block> POLYPORE_PLACEABLE_ON = create("polypore_placeable_on");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Hollow.id(name));
    }
}
