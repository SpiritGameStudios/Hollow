package dev.callmeecho.hollow.client;

import dev.callmeecho.hollow.main.registry.HollowEntityTypeRegistry;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import net.minecraft.util.math.MathHelper;

public class HollowDynamicLights implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights() {
        DynamicLightHandlers.registerDynamicLightHandler(HollowEntityTypeRegistry.FIREFLY, (entity) -> (int) MathHelper.clampedLerp(15.0F, 0.0F, (1.0F - entity.getLightTicks() / 10.0F)));
    }
}
