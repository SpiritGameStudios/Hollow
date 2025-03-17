package dev.spiritstudios.hollow.lambdynamiclights;

import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class FireflyEntityLuminance implements EntityLuminance {
    public static final FireflyEntityLuminance INSTANCE = new FireflyEntityLuminance();

    private FireflyEntityLuminance() {
    }

    @Override
    public @NotNull EntityLuminance.Type type() {
        return HollowDynamicLights.FIREFLY_LUMINANCE;
    }

    @Override
    public @Range(from = 0, to = 15) int getLuminance(@NotNull ItemLightSourceManager itemLightSourceManager, @NotNull Entity entity) {
        if (entity instanceof FireflyEntity firefly) {
            return MathHelper.floor(MathHelper.clampedLerp(15.0F, 0.0F, (1.0F - firefly.getLightTicks() / 10.0F)));
        }

        return 0;
    }
}