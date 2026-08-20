package dev.spiritstudios.hollow.client.data.gen;

import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import static dev.spiritstudios.hollow.client.data.gen.HollowTextureSlots.OVERLAY;
import static net.minecraft.client.data.models.model.TextureSlot.*;

public final class HollowTextureMappings {
    public static TextureMapping hollowLog(Block block) {
        if (!(block instanceof HollowLogBlock hollowLog)) {
            throw new IllegalArgumentException();
        }

        Identifier id = BuiltInRegistries.BLOCK.getKey(hollowLog.log);

        return new TextureMapping()
                .put(SIDE, TextureMapping.getBlockTexture(hollowLog.log))
                .put(INSIDE, new Material(id.withPrefix(hollowLog.isStripped ? "block/" : "block/stripped_")))
                .put(END, TextureMapping.getBlockTexture(hollowLog.log, "_top"));
    }

    public static TextureMapping hollowLogOverlay(Block block, Identifier overlay) {
        return hollowLog(block).put(OVERLAY, new Material(overlay));
    }
}
