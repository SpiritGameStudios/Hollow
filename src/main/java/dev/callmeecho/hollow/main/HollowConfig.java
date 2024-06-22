package dev.callmeecho.hollow.main;

import dev.callmeecho.cabinetapi.config.Config;
import net.minecraft.util.Identifier;

public class HollowConfig implements Config {
    @Override
    public Identifier getName() { return Identifier.of(Hollow.MODID, "config"); }
}
