package dev.spiritstudios.hollow.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.HollowConfig;
import dev.spiritstudios.hollow.data.HollowBiomeTags;
import dev.spiritstudios.specter.api.core.math.Easing;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow private static boolean fogEnabled;
    @Unique
    private static float prevEnd;

    @Unique
    private static float prevStart;

    @Unique
    private static float transitionProgress;

    @Unique
    private static final float DURATION = 50F;

    @WrapOperation(method = "applyFog", at = @At(value = "NEW", target = "(FFLnet/minecraft/client/render/FogShape;FFFF)Lnet/minecraft/client/render/Fog;"))
    private static Fog applyCloserFog(
            float fogStart, float fogEnd,
            FogShape fogShape,
            float r, float g, float b, float a,
            Operation<Fog> original,
            @Local(argsOnly = true) Camera camera,
            @Local(ordinal = 0, argsOnly = true) float viewDistance,
            @Local(ordinal = 1, argsOnly = true) float tickDelta
    ) {
        if (!HollowConfig.INSTANCE.closerFog.get()) return original.call(fogStart, fogEnd, fogShape, r, g, b, a);
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) return original.call(fogStart, fogEnd, fogShape, r, g, b, a);

        Entity entity = camera.getFocusedEntity();
        boolean closerFog = entity.getWorld().getBiome(entity.getBlockPos()).isIn(HollowBiomeTags.HAS_CLOSER_FOG);

        if (closerFog) {
            if (prevStart == 0.0F && prevEnd == 0.0F) {
                prevEnd = fogEnd;
                prevStart = fogStart;
                transitionProgress = 0.0F;
            }

            transitionProgress += tickDelta;
        } else if (transitionProgress > 0.0F) transitionProgress -= tickDelta;

        if (transitionProgress > 0.0F) {
            fogStart = (float) Easing.QUINT.out(
                    transitionProgress,
                    prevStart, viewDistance * 0.05F,
                    DURATION
            );

            fogEnd = (float) Easing.QUINT.out(
                    transitionProgress,
                    prevEnd, Math.min(viewDistance, 148) * 0.5F,
                    DURATION
            );

            fogShape = FogShape.SPHERE;
        }

        transitionProgress = MathHelper.clamp(transitionProgress, 0.0F, DURATION);
        return original.call(fogStart, fogEnd, fogShape, r, g, b, a);
    }
}
