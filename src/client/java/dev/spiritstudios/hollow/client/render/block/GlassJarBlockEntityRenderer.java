package dev.spiritstudios.hollow.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.hollow.world.level.block.BaseJarBlock;
import dev.spiritstudios.hollow.world.level.block.entity.GlassJarBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlassJarBlockEntityRenderer implements BlockEntityRenderer<GlassJarBlockEntity, GlassJarRenderState> {
	private static final float MAX_RANDOM_OFFSET = 0.02F;
	private static final float ITEM_SCALE = 0.35F;
	private static final float ITEM_SPACING = 0.01F;
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

		state.seed = blockEntity.getBlockState().getSeed(blockEntity.getBlockPos());

		state.items = new ArrayList<>();
		state.hanging = blockEntity.getBlockState().getValue(BaseJarBlock.HANGING);

		for (int slot = 0; slot < blockEntity.getContainerSize(); slot++) {
			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, blockEntity.getItem(slot), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, (int) state.seed + slot);
			state.items.add(itemState);
		}
    }

    @Override
    public void submit(GlassJarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		List<ItemStackRenderState> items = state.items;

		float deg = 0;
		int translationIndex = 0;

		for (ItemStackRenderState itemState : items) {
			if (itemState.isEmpty()) continue;

			poseStack.pushPose();

			float x = getRandomOffset(state.seed & 15L);
			float z = getRandomOffset(state.seed >> 8 & 15L);
			deg += getRandomOffset(state.seed >> 16 & 15L) * 5000.0F;

			poseStack.translate(0.5F + x, translationIndex * (ITEM_SCALE / 16.0F + ITEM_SPACING) + OFF_BOTTOM, 0.5F + z);

			if (state.hanging) poseStack.translate(0.0F, BaseJarBlock.HANGING_SHAPE_Y_DIFF, 0.0F);

			poseStack.mulPose(Axis.YP.rotationDegrees(deg));
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

			itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

			poseStack.popPose();
			translationIndex++;
		}
    }

	private static float getRandomOffset(long seed) {
		return Mth.clamp(((((float) seed - 0.5F) / 15.0F) - 0.5F) * 0.5F, -MAX_RANDOM_OFFSET, MAX_RANDOM_OFFSET);
	}
}
