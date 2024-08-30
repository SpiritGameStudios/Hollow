package dev.spiritstudios.hollow.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class HollowDataComponentRegistrar implements MinecraftRegistrar<ComponentType<?>> {
    public static final ComponentType<CopperInstrument> COPPER_INSTRUMENT = ComponentType.<CopperInstrument>builder()
            .codec(CopperInstrument.CODEC)
            .packetCodec(CopperInstrument.PACKET_CODEC)
            .build();

    public enum CopperInstrument implements StringIdentifiable {
        GREAT_SKY_FALLING(0, "great_sky_falling"),
        OLD_HYMN_RESTING(1, "old_hymn_resting"),
        PURE_WATER_DESIRE(2, "pure_water_desire"),
        HUMBLE_FIRE_MEMORY(3, "humble_fire_memory"),
        DRY_URGE_ANGER(4, "dry_urge_anger"),
        CLEAR_TEMPER_JOURNEY(5, "clear_temper_journey"),
        FRESH_NEST_THOUGHT(6, "fresh_nest_thought"),
        SECRET_LAKE_TEAR(7, "secret_lake_tear"),
        FEARLESS_RIVER_GIFT(8, "fearless_river_gift"),
        SWEET_MOON_LOVE(9, "sweet_moon_love");

        public static final Codec<CopperInstrument> CODEC = StringIdentifiable.createBasicCodec(CopperInstrument::values);
        public static final IntFunction<CopperInstrument> ID_TO_VALUE = ValueLists.createIdToValueFunction(CopperInstrument::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
        public static final PacketCodec<ByteBuf, CopperInstrument> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, value -> value.index);

        private final String name;
        private final int index;

        public final Map<String, SoundEvent> soundEvents;

        CopperInstrument(int index, String name) {
            this.name = name;
            this.index = index;

            this.soundEvents = Map.of(
                    "call", SoundEvent.of(Identifier.of("hollow", "horn.call.%d".formatted(index))),
                    "melody", SoundEvent.of(Identifier.of("hollow", "horn.melody.%d".formatted(index))),
                    "bass", SoundEvent.of(Identifier.of("hollow", "horn.bass.%d".formatted(index)))
            );
        }

        @Override
        public String asString() {
            return this.name;
        }

        public int getIndex() {
            return this.index;
        }
    }

    @Override
    public Registry<ComponentType<?>> getRegistry() {
        return Registries.DATA_COMPONENT_TYPE;
    }
}
