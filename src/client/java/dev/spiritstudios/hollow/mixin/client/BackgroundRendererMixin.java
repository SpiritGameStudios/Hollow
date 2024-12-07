package dev.spiritstudios.hollow.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.HollowConfig;
import dev.spiritstudios.hollow.HollowTags;
import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Unique
    private static float prevEnd;

    @Unique
    private static float prevStart;

    @Unique
    private static float transitionProgress;

    @Unique
    private static final float DURATION = 50F;

    @Inject(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
    private static void applyCloserFog(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            float viewDistance,
            boolean thickFog,
            float tickDelta,
            CallbackInfo ci,
            @Local BackgroundRenderer.FogData fogData,
            @Local Entity entity
    ) {
        if (!HollowConfig.INSTANCE.closerFog.get()) return;
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) return;
        boolean closerFog = entity.getWorld().getBiome(entity.getBlockPos()).isIn(HollowTags.CLOSER_FOG);

        if (closerFog) {
            if (prevStart == 0.0F && prevEnd == 0.0F) {
                prevEnd = fogData.fogEnd;
                prevStart = fogData.fogStart;
                transitionProgress = 0.0F;
            }

            transitionProgress += tickDelta;
        } else if (transitionProgress > 0.0F) transitionProgress -= tickDelta;

        if (transitionProgress > 0.0F) {
            fogData.fogStart = (float) Easing.SINE.inOut(
                    transitionProgress,
                    prevStart, viewDistance * 0.05F,
                    DURATION
            );

            fogData.fogEnd = (float) Easing.SINE.inOut(
                    transitionProgress,
                    prevEnd, Math.min(viewDistance, 148) * 0.5F,
                    DURATION
            );

            fogData.fogShape = FogShape.SPHERE;
        }

        transitionProgress = MathHelper.clamp(transitionProgress, 0.0F, DURATION);
    }
}
