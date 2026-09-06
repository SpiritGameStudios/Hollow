package dev.spiritstudios.hollow.client.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

import static net.minecraft.client.data.models.model.TextureSlot.*;

public final class HollowModelTemplates {
	public static final ModelTemplate HOLLOW_LOG = block("hollow_log", SIDE, INSIDE, END);
	public static final ModelTemplate HOLLOW_LOG_HORIZONTAL = block("hollow_log_horizontal", "_horizontal", SIDE, INSIDE, END);
	public static final ModelTemplate HOLLOW_LOG_LAYER = block("template_hollow_log_layer", TEXTURE);

	private static ModelTemplate make(TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.empty(), Optional.empty(), requiredTextureKeys);
	}

	private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(Hollow.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
	}

	private static ModelTemplate item(String parent, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(Hollow.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
	}

	private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(Optional.of(Hollow.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
	}
}
