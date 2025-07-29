package dev.spiritstudios.hollow.component;

import com.mojang.serialization.Codec;
import dev.spiritstudios.hollow.Hollow;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.Consumer;
import java.util.function.IntFunction;

// TODO: Make this data driven
public enum CopperInstrument implements StringIdentifiable, TooltipAppender {
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

    public static final IntFunction<CopperInstrument> ID_TO_VALUE = ValueLists.createIndexToValueFunction(
            CopperInstrument::getIndex,
            values(),
            ValueLists.OutOfBoundsHandling.ZERO
    );

    public static final Codec<CopperInstrument> CODEC = StringIdentifiable.createBasicCodec(CopperInstrument::values);
    public static final PacketCodec<ByteBuf, CopperInstrument> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, value -> value.index);

    private final String name;
    private final int index;

    public final SoundEvent call;
    public final SoundEvent melody;
    public final SoundEvent bass;

    CopperInstrument(int index, String name) {
        this.name = name;
        this.index = index;

        this.call = SoundEvent.of(Hollow.id("horn.call.%d".formatted(index)));
        this.melody = SoundEvent.of(Hollow.id("horn.melody.%d".formatted(index)));
        this.bass = SoundEvent.of(Hollow.id("horn.bass.%d".formatted(index)));
    }

    @Override
    public String asString() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        MutableText text = Text.translatable("copper_horn.%s".formatted(this.asString()));
        textConsumer.accept(text.formatted(Formatting.GRAY));
    }
}
