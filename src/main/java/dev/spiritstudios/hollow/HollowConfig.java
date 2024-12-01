package dev.spiritstudios.hollow;

import dev.spiritstudios.specter.api.config.Config;
import dev.spiritstudios.specter.api.config.ConfigHolder;
import dev.spiritstudios.specter.api.config.Value;

public class HollowConfig extends Config<HollowConfig> {
    public static final ConfigHolder<HollowConfig, ?> HOLDER = ConfigHolder.builder(
            Hollow.id("hollow"), HollowConfig.class
    ).build();
    public static final HollowConfig INSTANCE = HOLDER.get();

    public Value<Boolean> revertCopperBulb = booleanValue(true)
            .comment("Whether to revert the Copper Bulb to it's original 1-tick delay. If you aren't a redstoner, you can ignore this.")
            .sync()
            .build();

    public Value<Boolean> music = booleanValue(true)
            .comment("Whether to enable Hollow's custom music.")
            .build();
}
