package dev.spiritstudios.hollow.lambdynamiclights;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;

public class HollowDynamicLights implements DynamicLightsInitializer {
    public static final EntityLuminance.Type FIREFLY_LUMINANCE = EntityLuminance.Type.registerSimple(
            Hollow.id("firefly"),
            FireflyEntityLuminance.INSTANCE
    );

    @Override
    public void onInitializeDynamicLights(DynamicLightsContext context) {
        context.entityLightSourceManager().onRegisterEvent().register(registerContext -> registerContext.register(
                HollowEntityTypes.FIREFLY,
                FireflyEntityLuminance.INSTANCE
        ));
    }
}
