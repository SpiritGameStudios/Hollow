package dev.spiritstudios.hollow.data.component;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record CopperInstrumentComponent(Holder<CopperInstrument> instrument) implements TooltipProvider {
	public static final Codec<CopperInstrumentComponent> CODEC = CopperInstrument.CODEC.xmap(CopperInstrumentComponent::new, CopperInstrumentComponent::instrument);

	public static final StreamCodec<RegistryFriendlyByteBuf, CopperInstrumentComponent> STREAM_CODEC = CopperInstrument.STREAM_CODEC
			.map(CopperInstrumentComponent::new, CopperInstrumentComponent::instrument);

	@Override
	public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
		consumer.accept(ComponentUtils.mergeStyles(this.instrument.value().description(), Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}
}
