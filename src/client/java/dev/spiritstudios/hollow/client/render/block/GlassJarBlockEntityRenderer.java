package dev.spiritstudios.hollow.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.hollow.world.level.block.jar.BaseJarBlock;
import dev.spiritstudios.hollow.world.level.block.entity.GlassJarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlassJarBlockEntityRenderer implements BlockEntityRenderer<GlassJarBlockEntity, GlassJarRenderState> {
	private static final float ITEM_SCALE = 0.45F;
	private static final float ITEM_SPACING = 0.006F;
	private static final float OFF_BOTTOM = 0.05F;

    private final ItemModelResolver itemModelResolver;

    public GlassJarBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemModelResolver = ctx.itemModelResolver();
    }

    @Override
    public GlassJarRenderState createRenderState() {
        return new GlassJarRenderState();
    }

    @Override
    public void extractRenderState(GlassJarBlockEntity blockEntity, GlassJarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

		state.items = new ArrayList<>();
		state.seed = blockEntity.getBlockState().getSeed(blockEntity.getBlockPos());
		state.hanging = blockEntity.getBlockState().getValue(BaseJarBlock.HANGING);

		for (int slot = 0; slot < blockEntity.getContainerSize(); slot++) {
			ItemStackRenderState itemState = new ItemStackRenderState();
			ItemStack item = blockEntity.getItem(slot);

			if (!item.isEmpty()) {
				this.itemModelResolver.updateForTopItem(itemState, item, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, (int) state.seed + slot);
				state.items.add(itemState);
			}
		}
    }

    @Override
    public void submit(GlassJarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		List<ItemStackRenderState> items = state.items;

		int deg = Math.toIntExact(state.seed % 4) * 90;
		// int translationIndex = 0;

		for (int i = 0; i < items.size(); i++) {
			ItemStackRenderState itemState = items.get(i);

			poseStack.pushPose();
			poseStack.translate(0.5, i * (ITEM_SCALE / 16.0F + ITEM_SPACING) + OFF_BOTTOM, 0.5);

			if (state.hanging) {
				poseStack.translate(0.0F, BaseJarBlock.HANGING_SHAPE_Y_DIFF, 0.0F);
			}

			poseStack.mulPose(Axis.YP.rotationDegrees(deg = (deg + 90) % 360));
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

			poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
			itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}
}
