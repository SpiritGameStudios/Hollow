package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

import java.util.Optional;

import static dev.spiritstudios.hollow.datagen.HollowTextureKeys.OVERLAY;
import static net.minecraft.client.data.TextureKey.END;
import static net.minecraft.client.data.TextureKey.INSIDE;
import static net.minecraft.client.data.TextureKey.SIDE;

public final class HollowModels {
	public static final Model HOLLOW_LOG = block("hollow_log", SIDE, INSIDE, END);
	public static final Model HOLLOW_LOG_HORIZONTAL = block("hollow_log_horizontal", "_horizontal", SIDE, INSIDE, END);

	public static final Model HOLLOW_LOG_HORIZONTAL_MOSS = hollowLogHorizontalOverlay("moss");
	public static final Model HOLLOW_LOG_HORIZONTAL_PALE_MOSS = hollowLogHorizontalOverlay("pale_moss");
	public static final Model HOLLOW_LOG_HORIZONTAL_SNOW = hollowLogHorizontalOverlay("snow");

	private static Model make(TextureKey... requiredTextureKeys) {
		return new Model(Optional.empty(), Optional.empty(), requiredTextureKeys);
	}

	private static Model block(String parent, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(Hollow.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
	}

	private static Model item(String parent, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(Hollow.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
	}

	private static Model block(String parent, String variant, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(Hollow.id("block/" + parent)), Optional.of(variant), requiredTextureKeys);
	}

	private static Model hollowLogHorizontalOverlay(String overlay) {
		return block("hollow_log_horizontal_layer", "_horizontal_" + overlay, SIDE, INSIDE, END, OVERLAY);
	}
}
