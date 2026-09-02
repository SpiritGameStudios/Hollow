package dev.spiritstudios.hollow.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BoatRenderState;

public class FurnaceRaftRenderer extends FurnaceBoatRenderer{
	public FurnaceRaftRenderer(
		EntityRendererProvider.Context context,
		ModelLayerLocation modelId,
		ModelLayerLocation furnaceModelId
	) {
		super(context, modelId, furnaceModelId);
	}

	@Override
	protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
		this.submitFurnaceModel(state, poseStack, submitNodeCollector);
	}
}
