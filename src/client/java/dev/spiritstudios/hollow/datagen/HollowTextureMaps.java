package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import net.minecraft.block.Block;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.hollow.datagen.HollowTextureKeys.OVERLAY;
import static net.minecraft.client.data.TextureKey.END;
import static net.minecraft.client.data.TextureKey.INSIDE;
import static net.minecraft.client.data.TextureKey.SIDE;

public final class HollowTextureMaps {
	public static TextureMap hollowLog(Block block) {
		if (!(block instanceof HollowLogBlock hollowLog))
			throw new IllegalArgumentException();

		return new TextureMap()
				.put(SIDE, hollowLog.typeData.sideTexture())
				.put(INSIDE, hollowLog.typeData.insideTexture())
				.put(END, hollowLog.typeData.endTexture());
	}

	public static TextureMap hollowLogOverlay(Block block, Identifier overlay) {
		if (!(block instanceof HollowLogBlock hollowLog))
			throw new IllegalArgumentException();

		return new TextureMap()
				.put(SIDE, hollowLog.typeData.sideTexture())
				.put(INSIDE, hollowLog.typeData.insideTexture())
				.put(END, hollowLog.typeData.endTexture())
				.put(OVERLAY, overlay);
	}
}
