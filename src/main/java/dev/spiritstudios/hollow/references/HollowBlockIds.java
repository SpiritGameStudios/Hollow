package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public final class HollowBlockIds {
    public static final ResourceKey<Block> CATTAIL_STEM = create("cattail_stem");
	public static final ResourceKey<Block> GIANT_LILY_PAD = create("giant_lily_pad");

    private static ResourceKey<Block> create(String name) {
        return ResourceKey.create(Registries.BLOCK, Hollow.id(name));
    }
}
