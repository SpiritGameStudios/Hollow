package dev.spiritstudios.hollow;

import dev.spiritstudios.specter.api.config.Config;
import dev.spiritstudios.specter.api.config.ConfigHolder;
import dev.spiritstudios.specter.api.config.Value;

public final class HollowConfig extends Config<HollowConfig> {
    public static final ConfigHolder<HollowConfig, ?> HOLDER = ConfigHolder.builder(
            Hollow.id("hollow"), HollowConfig.class
    ).build();
    public static final HollowConfig INSTANCE = HOLDER.get();

    public final Value<Boolean> music = booleanValue(true)
            .comment("Whether to enable Hollow's custom music.")
            .build();

    public final Value<Boolean> closerFog = booleanValue(true)
            .comment("Moves the fog closer to the camera in some biomes")
            .build();
}
