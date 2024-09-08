package dev.spiritstudios.hollow.render.entity;

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
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class FireflyEntityRenderer extends EntityRenderer<FireflyEntity> {
    private static final Identifier TEXTURE = Identifier.of(MODID, "textures/entity/firefly.png");
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
        
        renderVertex(vertexConsumer, matrices.peek(), 0.0F, 0.0F, 0, 1, entity, light);
        renderVertex(vertexConsumer, matrices.peek(), 1.0F, 0.0F, 1, 1, entity, light);
        renderVertex(vertexConsumer, matrices.peek(), 1.0F, 1.0F, 1, 0, entity, light);
        renderVertex(vertexConsumer, matrices.peek(), 0.0F, 1.0F, 0, 0, entity, light);
        
        matrices.scale(1.25F, 1.25F, 1.25F);
        matrices.pop();
    }

    @Override
    protected int getBlockLight(FireflyEntity entity, BlockPos pos) {
        return 15;
    }

    private void renderVertex(VertexConsumer vertexConsumer, MatrixStack.Entry entry, float x, float y, float u, float v, FireflyEntity entity, int light) {
        float red;
        float green;
        float blue;

        if (entity.hasCustomName() && "jeb_".equals(entity.getName().getString())) {
            red = MathHelper.sin(entity.age * 0.1F) * 128.0F + 128.0F;
            green = MathHelper.sin(entity.age * 0.1F + (float) Math.toRadians(120.0F)) * 128.0F + 128.0F;
            blue = MathHelper.sin(entity.age * 0.1F + (float) Math.toRadians(240.0F)) * 128.0F + 128.0F;
        } else {
            int color = ColorHelper.Argb.lerp(MathHelper.clampedLerp(0.0F, 15.0F, (1.0F - entity.getLightTicks() / 10.0F)) / 15.0F, 0xFF92CF40, 0xFF30352F);

            red = ColorHelper.Argb.getRed(color);
            green = ColorHelper.Argb.getGreen(color);
            blue = ColorHelper.Argb.getBlue(color);
        }

        vertexConsumer.vertex(entry, x - 0.5F, y - 0.25F, 0.0F)
                .color(red / 255.0F, green / 255.0F, blue / 255.0F, 1.0F)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry, 0.0F, 1.0F, 0.0F);
    }
}
