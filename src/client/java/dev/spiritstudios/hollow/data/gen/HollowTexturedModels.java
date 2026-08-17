package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.client.data.models.model.TexturedModel;

public final class HollowTexturedModels {
	public static final TexturedModel.Provider HOLLOW_LOG = TexturedModel.createDefault(
			HollowTextureMappings::hollowLog,
			HollowModelTemplates.HOLLOW_LOG
	);

	public static final TexturedModel.Provider HOLLOW_LOG_HORIZONTAL = TexturedModel.createDefault(
			HollowTextureMappings::hollowLog,
			HollowModelTemplates.HOLLOW_LOG_HORIZONTAL
	);

	public static final TexturedModel.Provider HOLLOW_LOG_HORIZONTAL_MOSS = TexturedModel.createDefault(
			block -> HollowTextureMappings.hollowLogOverlay(block, Hollow.id("block/moss_overlay")),
			HollowModelTemplates.HOLLOW_LOG_HORIZONTAL_MOSS
	);

	public static final TexturedModel.Provider HOLLOW_LOG_HORIZONTAL_PALE_MOSS = TexturedModel.createDefault(
			block -> HollowTextureMappings.hollowLogOverlay(block, Hollow.id("block/pale_moss_overlay")),
			HollowModelTemplates.HOLLOW_LOG_HORIZONTAL_PALE_MOSS
	);

	public static final TexturedModel.Provider HOLLOW_LOG_HORIZONTAL_SNOW = TexturedModel.createDefault(
			block -> HollowTextureMappings.hollowLogOverlay(block, Hollow.id("block/snow_overlay")),
			HollowModelTemplates.HOLLOW_LOG_HORIZONTAL_SNOW
	);
}
