package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.component.CopperInstrument;
import net.minecraft.component.ComponentType;

public final class HollowDataComponentTypes {
    public static final ComponentType<CopperInstrument> COPPER_INSTRUMENT = ComponentType.<CopperInstrument>builder()
            .codec(CopperInstrument.CODEC)
            .packetCodec(CopperInstrument.PACKET_CODEC)
            .cache()
            .build();
}
