package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

import static dev.spiritstudios.hollow.data.gen.HollowTextureSlots.OVERLAY;
import static net.minecraft.client.data.models.model.TextureSlot.END;
import static net.minecraft.client.data.models.model.TextureSlot.INSIDE;
import static net.minecraft.client.data.models.model.TextureSlot.SIDE;

public final class HollowModelTemplates {
	public static final ModelTemplate HOLLOW_LOG = block("hollow_log", SIDE, INSIDE, END);
	public static final ModelTemplate HOLLOW_LOG_HORIZONTAL = block("hollow_log_horizontal", "_horizontal", SIDE, INSIDE, END);

	public static final ModelTemplate HOLLOW_LOG_HORIZONTAL_MOSS = hollowLogHorizontalOverlay("moss");
	public static final ModelTemplate HOLLOW_LOG_HORIZONTAL_PALE_MOSS = hollowLogHorizontalOverlay("pale_moss");
	public static final ModelTemplate HOLLOW_LOG_HORIZONTAL_SNOW = hollowLogHorizontalOverlay("snow");

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

	private static ModelTemplate hollowLogHorizontalOverlay(String overlay) {
		return block("hollow_log_horizontal_layer", "_horizontal_" + overlay, SIDE, INSIDE, END, OVERLAY);
	}
}
