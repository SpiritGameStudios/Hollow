package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.registry.HollowEntityTypeRegistrar;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.minecraft.util.math.MathHelper;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers;

public class HollowDynamicLights implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights() {
        DynamicLightHandlers.registerDynamicLightHandler(HollowEntityTypeRegistrar.FIREFLY, (entity) -> (int) MathHelper.clampedLerp(15.0F, 0.0F, (1.0F - entity.getLightTicks() / 10.0F)));
    }
}
