package dev.spiritstudios.hollow.render.entity;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class FireflyEntityRenderer extends EntityRenderer<FireflyEntity> {
    private static final Identifier TEXTURE = Hollow.id("textures/entity/firefly.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

    public FireflyEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(FireflyEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FireflyEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        matrices.push();
        matrices.multiply(this.dispatcher.getRotation());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);

        boolean isJeb = entity.hasCustomName() && "jeb_".equals(entity.getName().getString());

        float delta = MathHelper.clampedLerp(
                0.0F,
                1.0F,
                1.0F - entity.getLightTicks() / 10.0F
        );

        float r = !isJeb ?
                MathHelper.lerp(delta, 146, 48) :
                MathHelper.sin(entity.age * 0.1F) * 128F + 128F;

        float g = !isJeb ?
                MathHelper.lerp(delta, 207, 53) :
                MathHelper.sin(entity.age * 0.1F + (240.0F * MathHelper.RADIANS_PER_DEGREE)) * 128.0F + 128.0F;

        float b = !isJeb ?
                MathHelper.lerp(delta, 64, 47) :
                MathHelper.sin(entity.age * 0.1F + (120.0F * MathHelper.RADIANS_PER_DEGREE)) * 128.0F + 128.0F;

        r /= 255F;
        g /= 255F;
        b /= 255F;

        renderVertex(vertexConsumer, matrices.peek(), 0.0F, 0.0F, 0, 1, light, r, g, b);
        renderVertex(vertexConsumer, matrices.peek(), 1.0F, 0.0F, 1, 1, light, r, g, b);
        renderVertex(vertexConsumer, matrices.peek(), 1.0F, 1.0F, 1, 0, light, r, g, b);
        renderVertex(vertexConsumer, matrices.peek(), 0.0F, 1.0F, 0, 0, light, r, g, b);

        matrices.scale(1.25F, 1.25F, 1.25F);
        matrices.pop();
    }

    @Override
    protected int getBlockLight(FireflyEntity entity, BlockPos pos) {
        return 15;
    }

    private void renderVertex(
            VertexConsumer vertexConsumer,
            MatrixStack.Entry entry,
            float x,
            float y,
            float u,
            float v,
            int light,
            float r,
            float g,
            float b
    ) {
        vertexConsumer.vertex(entry, x - 0.5F, y - 0.25F, 0.0F)
                .color(r, g, b, 1.0F)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry, 0.0F, 1.0F, 0.0F);
    }
}
