package dev.spiritstudios.hollow;

import dev.spiritstudios.specter.api.config.Config;
import net.minecraft.util.Identifier;

public class HollowConfig extends Config<HollowConfig> {
    public static final HollowConfig INSTANCE = create(HollowConfig.class);

    @Override
    public Identifier getId() { return Identifier.of(Hollow.MODID, "hollow"); }

    public Value<Boolean> revertCopperBulb = booleanValue(true)
            .comment("Whether to revert the Copper Bulb to it's original 1-tick delay. If you aren't a redstoner, you can ignore this.")
            .sync()
            .build();

    public Value<Boolean> music = booleanValue(true)
            .comment("Whether to enable Hollow's custom music.")
            .build();
}
