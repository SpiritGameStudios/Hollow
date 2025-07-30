package dev.spiritstudios.hollow.component;

import net.minecraft.component.ComponentType;

public final class HollowDataComponentTypes {
    public static final ComponentType<CopperInstrumentComponent> COPPER_INSTRUMENT = ComponentType.<CopperInstrumentComponent>builder()
            .codec(CopperInstrumentComponent.CODEC)
            .packetCodec(CopperInstrumentComponent.PACKET_CODEC)
            .cache()
            .build();
}
