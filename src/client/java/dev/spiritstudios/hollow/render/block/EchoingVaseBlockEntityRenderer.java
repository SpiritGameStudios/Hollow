package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.specter.api.render.block.BlockModelBlockEntityRenderer;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class EchoingVaseBlockEntityRenderer extends BlockModelBlockEntityRenderer<EchoingVaseBlockEntity> {
    public EchoingVaseBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(EchoingVaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getCachedState().get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER)
            return;

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

        renderBlockModel(entity, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }
}
