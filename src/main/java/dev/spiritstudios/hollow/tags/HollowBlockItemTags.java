package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public final class HollowBlockItemTags {
    public static final BlockItemTagId HOLLOW_LOGS = create("hollow_logs");

    private static BlockItemTagId create(final String name) {
        Identifier id = Hollow.id(name);
        return BlockItemTagId.create(id, id);
    }
}
