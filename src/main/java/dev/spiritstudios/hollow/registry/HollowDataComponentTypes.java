package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowDataComponentTypes implements MinecraftRegistrar<ComponentType<?>> {
    public static final ComponentType<CopperInstrument> COPPER_INSTRUMENT = ComponentType.<CopperInstrument>builder()
            .codec(CopperInstrument.CODEC)
            .packetCodec(CopperInstrument.PACKET_CODEC)
            .build();

    @Override
    public Registry<ComponentType<?>> getRegistry() {
        return Registries.DATA_COMPONENT_TYPE;
    }

    @Override
    public Class<ComponentType<?>> getObjectType() {
        return Registrar.fixGenerics(ComponentType.class);
    }
}
