package dev.callmeecho.hollow;

import dev.callmeecho.cabinetapi.config.Config;
import dev.callmeecho.cabinetapi.config.annotations.Comment;
import dev.callmeecho.cabinetapi.config.annotations.Sync;
import net.minecraft.util.Identifier;

public class HollowConfig implements Config {
    @Override
    public Identifier getName() { return Identifier.of(Hollow.MODID, "config"); }

    @Sync
    @Comment("Whether to revert the Copper Bulb to it's original 1-tick delay. If you aren't a redstoner, you can ignore this.")
    public boolean revertCopperBulb = true;
}
