package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;

import static dev.spiritstudios.hollow.data.gen.HollowTextureSlots.OVERLAY;
import static net.minecraft.client.data.models.model.TextureSlot.END;
import static net.minecraft.client.data.models.model.TextureSlot.INSIDE;
import static net.minecraft.client.data.models.model.TextureSlot.SIDE;

public final class HollowTextureMappings {
	public static TextureMapping hollowLog(Block block) {
		if (!(block instanceof HollowLogBlock hollowLog)) {
            throw new IllegalArgumentException();
        }

		return new TextureMapping()
				.put(SIDE, new Material(hollowLog.typeData.sideTexture()))
				.put(INSIDE, new Material(hollowLog.typeData.insideTexture()))
				.put(END, new Material(hollowLog.typeData.endTexture()));
	}

	public static TextureMapping hollowLogOverlay(Block block, Identifier overlay) {
		if (!(block instanceof HollowLogBlock hollowLog))
			throw new IllegalArgumentException();

		return new TextureMapping()
				.put(SIDE, new Material(hollowLog.typeData.sideTexture()))
				.put(INSIDE, new Material(hollowLog.typeData.insideTexture()))
				.put(END, new Material(hollowLog.typeData.endTexture()))
				.put(OVERLAY, new Material(overlay));
	}
}
