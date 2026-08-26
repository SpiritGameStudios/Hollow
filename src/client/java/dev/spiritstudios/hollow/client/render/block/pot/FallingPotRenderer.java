package dev.spiritstudios.hollow.client.render.block.pot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.spiritstudios.hollow.world.level.block.entity.pot.FallingPotBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class FallingPotRenderer implements BlockEntityRenderer<FallingPotBlockEntity, FallingPotRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    protected final BlockModelResolver blockModelResolver;

	public static final float TILT_TIME = 0.5F;
	public static final float FALL_TIME = 1F;


	public FallingPotRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    private static final float tiltAngle = 0.6283f / 2;
    private static final float fallAngle = Mth.HALF_PI - tiltAngle;

    @Override
    public FallingPotRenderState createRenderState() {
        return new FallingPotRenderState();
    }

    @Override
    public void extractRenderState(FallingPotBlockEntity blockEntity, FallingPotRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getDirection();
        DecoratedPotBlockEntity.WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
        if (wobbleStyle != null && blockEntity.getLevel() != null) {
            state.wobbleProgress = ((float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
        } else {
            state.wobbleProgress = 0.0F;
        }

        state.fallProgress = blockEntity.fallStartedAtTick == -1 ?
			0.0F :
			((float) (blockEntity.getLevel().getGameTime() - blockEntity.fallStartedAtTick) + partialTicks) / FallingPotBlockEntity.FALL_DURATION;
		state.fallDirection = blockEntity.fallDirection;
		state.half = blockEntity.getBlockState().getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);

        blockModelResolver.update(state.model, blockEntity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(FallingPotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        if (state.fallProgress > 0) {
			Vector3f fallDir = state.fallDirection.step();

			if (state.fallProgress >= TILT_TIME) {
				float pct = Math.min(1, Ease.inQuart((state.fallProgress - TILT_TIME) / (FALL_TIME - TILT_TIME)));

				float angle = Math.min(
						tiltAngle + fallAngle * pct,
						Mth.HALF_PI
				);

				poseStack.rotateAround(
						Axis.of(state.fallDirection.getCounterClockWise().step()).rotation(angle),
						0.5f + fallDir.x / 2, state.half.equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
				);
			} else {
				float angle = tiltAngle * Ease.inOutSine(state.fallProgress / TILT_TIME);

				poseStack.rotateAround(
						Axis.of(state.fallDirection.getCounterClockWise(net.minecraft.core.Direction.Axis.Y).step()).rotation(angle),
						0.5f + fallDir.x / 2, state.half.equals(DoubleBlockHalf.LOWER) ? 0 : -1, 0.5f + fallDir.z / 2
				);
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
