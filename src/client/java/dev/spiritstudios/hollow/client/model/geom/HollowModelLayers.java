package dev.spiritstudios.hollow.client.model.geom;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.client.model.object.boat.FurnaceBoatModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class HollowModelLayers {
	public static final ModelLayerLocation FURNACE_BOAT = register(
		"boat", "furnace",
		FurnaceBoatModel::createFurnaceBoatModel
	);

	public static final ModelLayerLocation FURNACE_RAFT = register(
		"raft", "furnace",
		FurnaceBoatModel::createFurnaceRaftModel
	);

	private HollowModelLayers() {
	}

	private static ModelLayerLocation register(String path, String layer, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
		ModelLayerLocation modelLayer = new ModelLayerLocation(Hollow.id(path), layer);
		ModelLayerRegistry.registerModelLayer(modelLayer, provider);

		return modelLayer;
	}

	public static void init() {
		// NO-OP
	}
}
