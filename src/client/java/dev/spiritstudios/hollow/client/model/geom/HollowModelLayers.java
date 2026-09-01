package dev.spiritstudios.hollow.client.model.geom;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.client.model.object.boat.FurnaceBoatModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class HollowModelLayers {

	public static final ModelLayerLocation ACACIA_FURNACE_BOAT = registerFurnaceBoat("acacia", false);
	public static final ModelLayerLocation BAMBOO_FURNACE_RAFT = registerFurnaceBoat("bamboo", true);
	public static final ModelLayerLocation BIRCH_FURNACE_BOAT = registerFurnaceBoat("birch", false);
	public static final ModelLayerLocation CHERRY_FURNACE_BOAT = registerFurnaceBoat("cherry", false);
	public static final ModelLayerLocation DARK_OAK_FURNACE_BOAT = registerFurnaceBoat("dark_oak", false);
	public static final ModelLayerLocation JUNGLE_FURNACE_BOAT = registerFurnaceBoat("jungle", false);
	public static final ModelLayerLocation MANGROVE_FURNACE_BOAT = registerFurnaceBoat("mangrove", false);
	public static final ModelLayerLocation OAK_FURNACE_BOAT = registerFurnaceBoat("oak", false);
	public static final ModelLayerLocation PALE_OAK_FURNACE_BOAT = registerFurnaceBoat("pale_oak", false);
	public static final ModelLayerLocation SPRUCE_FURNACE_BOAT = registerFurnaceBoat("spruce", false);

	private HollowModelLayers() {}

	private static ModelLayerLocation registerFurnaceBoat(String path, boolean raft) {
		return register("furnace_boat/" + path, raft ? FurnaceBoatModel::createFurnaceRaftModel : FurnaceBoatModel::createFurnaceBoatModel);
	}

	private static ModelLayerLocation register(String path, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
		ModelLayerLocation modelLayer = new ModelLayerLocation(Hollow.id(path), "main");
		ModelLayerRegistry.registerModelLayer(modelLayer, provider);

		return modelLayer;
	}

	public static void init() {
		// NO-OP
	}
}
