package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.block.entity.EchoingPotBlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.Objects;

public class EchoingPotBlockEntityRenderer implements BlockEntityRenderer<EchoingPotBlockEntity> {
    private final BlockRenderManager renderManager;

    public EchoingPotBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.renderManager = context.getRenderManager();
    }

    @Override
    public void render(EchoingPotBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        DecoratedPotBlockEntity.WobbleType wobbleType = entity.lastWobbleType;
        if (wobbleType != null && entity.getWorld() != null) {
            float wobbleProgress = ((float) (entity.getWorld().getTime() - entity.lastWobbleTime) + tickDelta) / (float) wobbleType.lengthInTicks;
            if (wobbleProgress >= 0.0F && wobbleProgress <= 1.0F) {
                if (wobbleType == DecoratedPotBlockEntity.WobbleType.POSITIVE) {
                    float progressRadians = wobbleProgress * MathHelper.TAU;

                    matrices.multiply(
                            RotationAxis.POSITIVE_X.rotation((-1.5F * (MathHelper.cos(progressRadians) + 0.5F) * MathHelper.sin(progressRadians / 2.0F)) * 0.015625F),
                            0.5F, 0.0F, 0.5F
                    );

                    matrices.multiply(
                            RotationAxis.POSITIVE_Z.rotation(MathHelper.sin(progressRadians) * 0.015625F),
                            0.5F, 0.0F, 0.5F
                    );
                } else matrices.multiply(
                        RotationAxis.POSITIVE_Y.rotation(MathHelper.sin(-wobbleProgress * 3.0F * MathHelper.PI) * 0.125F * (1.0F - wobbleProgress)),
                        0.5F, 0.0F, 0.5F
                );
            }
        }

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(entity.getCachedState()));

        renderManager.getModelRenderer().render(
                entity.getWorld(),
                renderManager.getModel(entity.getCachedState()),
                entity.getCachedState(),
                entity.getPos(),
                matrices,
                vertexConsumer,
                true,
                Objects.requireNonNull(entity.getWorld()).getRandom(),
                light,
                overlay
        );

        matrices.pop();
    }
}
