package dev.spiritstudios.hollow.tags;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;

public final class HollowBlockItemTags {
    public static final BlockItemTagId HOLLOW_LOGS = create("hollow_logs");
	public static final BlockItemTagId GLASS_JARS = create("glass_jars");
	public static final BlockItemTagId FORMS_GIANT_LILY_PAD = create("forms_giant_lily_pad");

    private static BlockItemTagId create(final String name) {
        Identifier id = Hollow.id(name);
        return BlockItemTagId.create(id, id);
    }
}
