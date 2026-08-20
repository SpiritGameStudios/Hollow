package dev.spiritstudios.hollow.client.render.block;

import dev.spiritstudios.hollow.world.level.block.entity.EchoingPotBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class EchoingPotBlockEntityRenderer implements BlockEntityRenderer<EchoingPotBlockEntity, EchoingPotRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    protected final BlockModelResolver blockModelResolver;

    public EchoingPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public EchoingPotRenderState createRenderState() {
        return new EchoingPotRenderState();
    }

    @Override
    public void extractRenderState(EchoingPotBlockEntity blockEntity, EchoingPotRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getDirection();
        DecoratedPotBlockEntity.WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
        if (wobbleStyle != null && blockEntity.getLevel() != null) {
            state.wobbleProgress = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
        } else {
            state.wobbleProgress = 0.0F;
        }

        blockModelResolver.update(state.model, blockEntity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(EchoingPotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(DecoratedPotRenderer.modelTransformation(state.direction));
        if (state.wobbleProgress >= 0.0F && state.wobbleProgress <= 1.0F) {
            if (state.wobbleStyle == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
                final float amplitude = 0.015625F;
                float deltaTime = state.wobbleProgress * Mth.TWO_PI;
                float tiltX = -1.5F * (Mth.cos(deltaTime) + 0.5F) * Mth.sin(deltaTime / 2.0F);
                poseStack.rotateAround(Axis.XP.rotation(tiltX * amplitude), 0.5F, 0.0F, 0.5F);
                float tiltZ = Mth.sin(deltaTime);
                poseStack.rotateAround(Axis.ZP.rotation(tiltZ * amplitude), 0.5F, 0.0F, 0.5F);
            } else {
                float turnAngle = Mth.sin(-state.wobbleProgress * 3.0F * Mth.PI) * 0.125F;
                float linearDecayFactor = 1.0F - state.wobbleProgress;
                poseStack.rotateAround(Axis.YP.rotation(turnAngle * linearDecayFactor), 0.5F, 0.0F, 0.5F);
            }
        }

        state.model.submit(
                poseStack,
                submitNodeCollector,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0x00000000
        );

        poseStack.popPose();
    }
}
