package dev.spiritstudios.hollow.client.render.entity;

import dev.spiritstudios.hollow.client.model.object.boat.FurnaceBoatModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;

public class FurnaceBoatRenderer extends BoatRenderer {
	private final FurnaceBoatModel model;

	public FurnaceBoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
		super(context, modelId);
		this.model = new FurnaceBoatModel(context.bakeLayer(modelId));
	}

	@Override
	protected EntityModel<BoatRenderState> model() {
		return this.model;
	}
}
