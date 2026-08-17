package dev.spiritstudios.hollow.core.component;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public final class HollowDataComponents {
    public static final DataComponentType<CopperInstrumentComponent> COPPER_INSTRUMENT = register(
            "copper_instrument",
            builder -> builder
                    .persistent(CopperInstrumentComponent.CODEC)
                    .networkSynchronized(CopperInstrumentComponent.STREAM_CODEC)
                    .cacheEncoding()
    );

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Hollow.id(id), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {
        // NO-OP
    }
}
