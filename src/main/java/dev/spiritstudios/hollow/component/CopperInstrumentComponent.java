package dev.spiritstudios.hollow.component;

import com.mojang.serialization.Codec;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import java.util.Optional;
import java.util.function.Consumer;

public record CopperInstrumentComponent(
		LazyRegistryEntryReference<CopperInstrument> instrument) implements TooltipAppender {
	public static final Codec<CopperInstrumentComponent> CODEC = LazyRegistryEntryReference.createCodec(
					HollowRegistryKeys.COPPER_INSTRUMENT,
					CopperInstrument.ENTRY_CODEC
			)
			.xmap(CopperInstrumentComponent::new, CopperInstrumentComponent::instrument);

	public static final PacketCodec<RegistryByteBuf, CopperInstrumentComponent> PACKET_CODEC = LazyRegistryEntryReference.createPacketCodec(
					HollowRegistryKeys.COPPER_INSTRUMENT,
					CopperInstrument.ENTRY_PACKET_CODEC
			)
			.xmap(CopperInstrumentComponent::new, CopperInstrumentComponent::instrument);

	public CopperInstrumentComponent(RegistryKey<CopperInstrument> instrument) {
		this(new LazyRegistryEntryReference<>(instrument));
	}

	public CopperInstrumentComponent(RegistryEntry<CopperInstrument> instrument) {
		this(new LazyRegistryEntryReference<>(instrument));
	}

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		RegistryWrapper.WrapperLookup wrapperLookup = context.getRegistryLookup();
		if (wrapperLookup != null) {
			Optional<RegistryEntry<CopperInstrument>> optional = this.getInstrument(wrapperLookup);
			if (optional.isPresent()) {
				MutableText mutableText = optional.get().value().description().copy();
				Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
				textConsumer.accept(mutableText);
			}
		}
	}

	public Optional<RegistryEntry<CopperInstrument>> getInstrument(RegistryWrapper.WrapperLookup registries) {
		return this.instrument.resolveEntry(registries);
	}
}
