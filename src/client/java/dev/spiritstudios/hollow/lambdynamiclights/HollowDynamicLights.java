package dev.spiritstudios.hollow.lambdynamiclights;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import net.minecraft.util.math.MathHelper;

public class HollowDynamicLights implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager itemLightSourceManager) {
        DynamicLightHandlers.registerDynamicLightHandler(
                HollowEntityTypes.FIREFLY,
                entity -> (int) MathHelper.clampedLerp(15.0F, 0.0F, (1.0F - entity.getLightTicks() / 10.0F))
        );
    }
}