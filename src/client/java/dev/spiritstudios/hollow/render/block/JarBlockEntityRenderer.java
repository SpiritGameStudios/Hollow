package dev.spiritstudios.hollow.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.hollow.world.level.block.entity.JarBlockEntity;
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

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity, JarRenderState> {
    private final ItemModelResolver itemModelResolver;

    public JarBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemModelResolver = ctx.itemModelResolver();
    }

    @Override
    public JarRenderState createRenderState() {
        return new JarRenderState();
    }

    @Override
    public void extractRenderState(JarBlockEntity blockEntity, JarRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        state.items = new ArrayList<>();

        for (int slot = 0; slot < blockEntity.getItems().size(); slot++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemState, blockEntity.getItems().get(slot), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, slot);
            state.items.add(itemState);
        }
    }

    @Override
    public void submit(JarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.items.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.05F, 0.5F);
        poseStack.scale(0.45F, 0.45F, 0.45F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));


        int index = 0;
        float deg = 0;

        for (ItemStackRenderState itemState : state.items) {
            poseStack.translate(0.0F, 0.0F, -0.0625F);
            long hashCode = Mth.getSeed(state.blockPos.getX(), index, state.blockPos.getY());
            float max = 0.05F;

            double x = Mth.clamp(((double) ((float) (hashCode & 15L) / 15.0F) - 0.5) * 0.5, -max, max);
            double z = Mth.clamp(((double) ((float) (hashCode >> 8 & 15L) / 15.0F) - 0.5) * 0.5, -max, max);

            deg += Mth.clamp(((double) ((float) (hashCode >> 16 & 15L) / 15.0F) - 0.5) * 0.5, -max, max) * 5000;

            poseStack.pushPose();
            poseStack.translate(x, z, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(deg));

            itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
            index++;
        }

        poseStack.popPose();
    }
}
