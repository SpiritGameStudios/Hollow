package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class HollowBlockIds {
    public static final ResourceKey<Block> CATTAIL_STEM = create("cattail_stem");

    private static ResourceKey<Block> create(String name) {
        return ResourceKey.create(Registries.BLOCK, Hollow.id(name));
    }
}
