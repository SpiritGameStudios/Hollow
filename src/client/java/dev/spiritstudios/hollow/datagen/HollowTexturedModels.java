package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.client.data.TexturedModel;

public final class HollowTexturedModels {
	public static final TexturedModel.Factory HOLLOW_LOG = TexturedModel.makeFactory(
			HollowTextureMaps::hollowLog,
			HollowModels.HOLLOW_LOG
	);

	public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL = TexturedModel.makeFactory(
			HollowTextureMaps::hollowLog,
			HollowModels.HOLLOW_LOG_HORIZONTAL
	);

	public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_MOSS = TexturedModel.makeFactory(
			block -> HollowTextureMaps.hollowLogOverlay(block, Hollow.id("block/moss_overlay")),
			HollowModels.HOLLOW_LOG_HORIZONTAL_MOSS
	);

	public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_PALE_MOSS = TexturedModel.makeFactory(
			block -> HollowTextureMaps.hollowLogOverlay(block, Hollow.id("block/pale_moss_overlay")),
			HollowModels.HOLLOW_LOG_HORIZONTAL_PALE_MOSS
	);

	public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_SNOW = TexturedModel.makeFactory(
			block -> HollowTextureMaps.hollowLogOverlay(block, Hollow.id("block/snow_overlay")),
			HollowModels.HOLLOW_LOG_HORIZONTAL_SNOW
	);
}
