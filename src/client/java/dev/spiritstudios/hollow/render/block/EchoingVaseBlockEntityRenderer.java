package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.specter.api.core.math.Easing;
import dev.spiritstudios.specter.api.render.block.BlockModelBlockEntityRenderer;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class EchoingVaseBlockEntityRenderer extends BlockModelBlockEntityRenderer<EchoingVaseBlockEntity> {
    public EchoingVaseBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    private static final float tiltAngle = 0.6283f / 2;
    private static final float fallAngle = MathHelper.HALF_PI - tiltAngle;

    @Override
    public void render(EchoingVaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        DecoratedPotBlockEntity.WobbleType wobbleType = entity.lastWobbleType;
        if (entity.getWorld() == null) {
            renderBlockModel(entity, matrices, vertexConsumers, light, overlay);

            matrices.pop();
            return;
        }

        if (entity.fallTime > 0) {
            Vector3f fallDir = entity.fallDirection.getUnitVector();

            Hollow.LOGGER.info(String.valueOf(entity.fallTime));
            if (entity.fallTime >= EchoingVaseBlockEntity.TILT_TIME) {
                float pct = (float) Math.min(1, Easing.EXP.in((entity.fallTime - EchoingVaseBlockEntity.TILT_TIME + tickDelta) / (EchoingVaseBlockEntity.FALL_TIME - EchoingVaseBlockEntity.TILT_TIME)));

                float angle = Math.min(
                        tiltAngle + fallAngle * pct,
                        MathHelper.HALF_PI
                );

                matrices.multiply(
                        RotationAxis.of(entity.fallDirection.rotateYCounterclockwise().getUnitVector()).rotation(angle),
                        0.5f + fallDir.x / 2, entity.getCachedState().get(Properties.DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
                );
            } else {
                float pct = (entity.fallTime + tickDelta) / EchoingVaseBlockEntity.TILT_TIME;
                float angle = (float) (tiltAngle * Easing.QUAD.inOut(pct));

                matrices.multiply(
                        RotationAxis.of(entity.fallDirection.rotateCounterclockwise(Direction.Axis.Y).getUnitVector()).rotation(angle),
                        0.5f + fallDir.x / 2, entity.getCachedState().get(Properties.DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
                );
            }
        } else if (wobbleType != null) {
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

    @Override
    public boolean rendersOutsideBoundingBox(EchoingVaseBlockEntity blockEntity) {
        return true;
    }
}
