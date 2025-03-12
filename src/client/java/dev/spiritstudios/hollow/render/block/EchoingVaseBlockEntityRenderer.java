package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.specter.api.core.math.Easing;
import dev.spiritstudios.specter.api.render.block.BlockModelBlockEntityRenderer;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class EchoingVaseBlockEntityRenderer extends BlockModelBlockEntityRenderer<EchoingVaseBlockEntity> {
    public EchoingVaseBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    private static float tiltAngle = 0.6283f / 2;
    private static float fallAngle = MathHelper.HALF_PI - tiltAngle;

    @Override
    public void render(EchoingVaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        DecoratedPotBlockEntity.WobbleType wobbleType = entity.lastWobbleType;
        if (entity.getWorld() != null) {
            if (entity.fallTime > 0) {
                entity.fallTime++;


                Vector3f fallDir = entity.fallDirection.getUnitVector();
                if (entity.fallTime > EchoingVaseBlockEntity.TILT_TIME) {
//                    float angle = Math.max(
//                            0,
//                            MathHelper.HALF_PI
//                    );
                    float pct = (float) Math.min(1, Easing.QUINT.in((entity.fallTime - EchoingVaseBlockEntity.TILT_TIME + tickDelta) / (EchoingVaseBlockEntity.FALL_TIME - EchoingVaseBlockEntity.TILT_TIME)));
                    float angle = Math.min(
                            tiltAngle + fallAngle * pct,
                            MathHelper.HALF_PI
                    );
                    matrices.multiply(
                            RotationAxis.of(entity.fallDirection.rotateCounterclockwise(Direction.Axis.Y).getUnitVector()).rotation(angle),
                            0.5f + fallDir.x / 2, entity.getCachedState().get(Properties.DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
                    );

                    if (entity.fallTime > EchoingVaseBlockEntity.FALL_TIME && !entity.fallen) {
                        entity.fallen = true;
                        entity.getWorld().playSoundAtBlockCenter(entity.getPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1, 1, true);
                        entity.getWorld().playSoundAtBlockCenter(entity.getPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1, 1, true);
                        for (int i = 0; i < 100; i++) {
                            entity.getWorld().addBlockBreakParticles(entity.getPos(), entity.getCachedState());
                        }
                    }
                } else {
                    float pct = Math.min((entity.fallTime + tickDelta) / EchoingVaseBlockEntity.TILT_TIME, 1);
                    float angle = (float) (tiltAngle * Easing.CIRC.inOut(pct));
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
        }

        renderBlockModel(entity, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(EchoingVaseBlockEntity blockEntity) {
        return true;
    }
}
