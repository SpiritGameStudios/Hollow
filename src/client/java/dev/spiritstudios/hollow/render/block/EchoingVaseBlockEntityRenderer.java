package dev.spiritstudios.hollow.render.block;

import dev.spiritstudios.hollow.world.level.block.entity.EchoingVaseBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Ease;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class EchoingVaseBlockEntityRenderer implements BlockEntityRenderer<EchoingVaseBlockEntity, EchoingVaseRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    protected final BlockModelResolver blockModelResolver;

    public EchoingVaseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    private static final float tiltAngle = 0.6283f / 2;
    private static final float fallAngle = Mth.HALF_PI - tiltAngle;

    @Override
    public EchoingVaseRenderState createRenderState() {
        return new EchoingVaseRenderState();
    }

    @Override
    public void extractRenderState(EchoingVaseBlockEntity blockEntity, EchoingVaseRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getDirection();
        DecoratedPotBlockEntity.WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
        if (wobbleStyle != null && blockEntity.getLevel() != null) {
            state.wobbleProgress = ((float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
        } else {
            state.wobbleProgress = 0.0F;
        }

        state.fallTime = blockEntity.fallTime + partialTicks;
		state.fallDirection = blockEntity.fallDirection;
		state.half = blockEntity.getBlockState().getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);

        blockModelResolver.update(state.model, blockEntity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(EchoingVaseRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        if (state.fallTime > 0) {
			Vector3f fallDir = state.fallDirection.step();

			if (state.fallTime >= EchoingVaseBlockEntity.TILT_TIME) {
				float pct = Math.min(1, Ease.inQuart((state.fallTime - EchoingVaseBlockEntity.TILT_TIME) / (EchoingVaseBlockEntity.FALL_TIME - EchoingVaseBlockEntity.TILT_TIME)));

				float angle = Math.min(
						tiltAngle + fallAngle * pct,
						Mth.HALF_PI
				);

				poseStack.rotateAround(
						Axis.of(state.fallDirection.getCounterClockWise().step()).rotation(angle),
						0.5f + fallDir.x / 2, state.half.equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
				);
			} else {
				float pct = state.fallTime / EchoingVaseBlockEntity.TILT_TIME;
				float angle = tiltAngle * Ease.inOutSine(pct);

				poseStack.rotateAround(
						Axis.of(state.fallDirection.getCounterClockWise(net.minecraft.core.Direction.Axis.Y).step()).rotation(angle),
						0.5f + fallDir.x / 2, state.half.equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
				);
			}
		}

        poseStack.popPose();
    }
}