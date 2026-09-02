package dev.spiritstudios.hollow.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.spiritstudios.hollow.client.model.geom.HollowModelLayers;
import dev.spiritstudios.hollow.client.model.object.boat.FurnaceBoatModel;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RaftRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

public class FurnaceRaftRenderer extends RaftRenderer  {
	private final FurnaceBoatModel furnaceModel;

	public FurnaceRaftRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
		super(context, modelId);
		this.furnaceModel = new FurnaceBoatModel(context.bakeLayer(HollowModelLayers.FURNACE_RAFT));
	}

	@Override
	public void extractRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		if (entity instanceof AbstractFurnaceBoat furnaceBoat) FurnaceBoatModel.extractFuelRenderState(furnaceBoat, state, partialTicks);
	}

	@Override
	protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
		super.submitTypeAdditions(state, poseStack, submitNodeCollector, lightCoords);
		submitNodeCollector.submitModel(this.furnaceModel, state, poseStack, FurnaceBoatRenderer.FURNACE_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
	}
}
