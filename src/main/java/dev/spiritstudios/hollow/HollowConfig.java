package dev.spiritstudios.hollow;

import dev.spiritstudios.specter.api.config.Config;
import dev.spiritstudios.specter.api.config.annotations.Comment;
import dev.spiritstudios.specter.api.config.annotations.Sync;
import net.minecraft.util.Identifier;

public class HollowConfig implements Config {
    @Override
    public Identifier getId() { return Identifier.of(Hollow.MODID, "config"); }

    @Sync
    @Comment("Whether to revert the Copper Bulb to it's original 1-tick delay. If you aren't a redstoner, you can ignore this.")
    public boolean revertCopperBulb = true;

    @Comment("Whether to enable Hollow's custom music.")
    public boolean music = true;
}
