package dev.spiritstudios.hollow.client.data.gen;

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
}
