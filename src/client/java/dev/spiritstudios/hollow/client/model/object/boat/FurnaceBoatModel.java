package dev.spiritstudios.hollow.client.model.object.boat;

import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.util.Mth;

public class FurnaceBoatModel extends EntityModel<BoatRenderState> {
	public static final RenderStateDataKey<Boolean> HAS_FUEL = RenderStateDataKey.create(() -> "has_fuel");
	private static final String FURNACE_NAME = "furnace";
	private static final String LIT_FURNACE_NAME = "lit_furnace";

	private final ModelPart furnace;
	private final ModelPart litFurnace;

	public FurnaceBoatModel(ModelPart root) {
		super(root);
		this.furnace = root.getChild(FURNACE_NAME);
		this.litFurnace = root.getChild(LIT_FURNACE_NAME);
	}

	public static void extractFuelRenderState(AbstractFurnaceBoat entity, BoatRenderState state, float partialTicks) {
		state.setData(FurnaceBoatModel.HAS_FUEL, entity.hasFuel());
	}

	@Override
	public void setupAnim(BoatRenderState state) {
		super.setupAnim(state);

		if (state.getDataOrDefault(HAS_FUEL, false)) {
			this.furnace.visible = false;
			this.litFurnace.visible = true;

			this.litFurnace.xRot = 0.01F * Mth.triangleWave(state.ageInTicks, 2.6F);
			this.litFurnace.yRot = -Mth.HALF_PI + 0.01F * Mth.sin(state.ageInTicks * Mth.PI * 0.7);
		} else {
			this.furnace.visible = true;
			this.litFurnace.visible = false;
		}
	}

	public static LayerDefinition createFurnaceRaftModel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild(
			FURNACE_NAME,
			CubeListBuilder.create()
				.addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offsetAndRotation(-8.0F, -2.1F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F)
		);
		root.addOrReplaceChild(
			LIT_FURNACE_NAME,
			CubeListBuilder.create()
				.texOffs(0, 24)
				.addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offsetAndRotation(-8.0F, -2.1F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F)
		);

		return LayerDefinition.create(mesh, 64, 64);
	}

	public static LayerDefinition createFurnaceBoatModel() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild(
			FURNACE_NAME,
			CubeListBuilder.create()
				.addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offsetAndRotation(-8.0F, 3.0F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F)
		);
		root.addOrReplaceChild(
			LIT_FURNACE_NAME,
			CubeListBuilder.create()
				.texOffs(0, 24)
				.addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offsetAndRotation(-8.0F, 3.0F, 0.0F, 0.0F, -Mth.HALF_PI, 0.0F)
		);

		return LayerDefinition.create(mesh, 64, 64);
	}
}
