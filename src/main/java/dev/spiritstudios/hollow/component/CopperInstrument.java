package dev.spiritstudios.hollow.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

// TODO: Make this data driven
public record CopperInstrument(
		RegistryEntry<SoundEvent> call,
		RegistryEntry<SoundEvent> melody,
		RegistryEntry<SoundEvent> bass,
		float useDuration, float range,
		Text description
) {
	public static CopperInstrument of(String namespace, String call, String melody, String bass, float useDuration, float range) {
		return new CopperInstrument(
				Registries.SOUND_EVENT.getEntry(Identifier.of(namespace, "item.copper_horn.call." + call)).orElseThrow(),
				Registries.SOUND_EVENT.getEntry(Identifier.of(namespace, "item.copper_horn.melody." + melody)).orElseThrow(),
				Registries.SOUND_EVENT.getEntry(Identifier.of(namespace, "item.copper_horn.bass." + bass)).orElseThrow(),
				useDuration, range,
				Text.translatable("item.hollow.copper_horn." + namespace + "." + call + "_" + melody + "_" + bass)
		);
	}

	public static final Codec<CopperInstrument> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							SoundEvent.ENTRY_CODEC.fieldOf("call").forGetter(CopperInstrument::call),
							SoundEvent.ENTRY_CODEC.fieldOf("melody").forGetter(CopperInstrument::melody),
							SoundEvent.ENTRY_CODEC.fieldOf("bass").forGetter(CopperInstrument::bass),
							Codecs.POSITIVE_FLOAT.fieldOf("use_duration").forGetter(CopperInstrument::useDuration),
							Codecs.POSITIVE_FLOAT.fieldOf("range").forGetter(CopperInstrument::range),
							TextCodecs.CODEC.fieldOf("description").forGetter(CopperInstrument::description)
					)
					.apply(instance, CopperInstrument::new)
	);

	public static final PacketCodec<RegistryByteBuf, CopperInstrument> PACKET_CODEC = PacketCodec.tuple(
			SoundEvent.ENTRY_PACKET_CODEC, CopperInstrument::call,
			SoundEvent.ENTRY_PACKET_CODEC, CopperInstrument::melody,
			SoundEvent.ENTRY_PACKET_CODEC, CopperInstrument::bass,
			PacketCodecs.FLOAT, CopperInstrument::useDuration,
			PacketCodecs.FLOAT, CopperInstrument::range,
			TextCodecs.REGISTRY_PACKET_CODEC, CopperInstrument::description,
			CopperInstrument::new
	);

	public static final Codec<RegistryEntry<CopperInstrument>> ENTRY_CODEC = RegistryElementCodec.of(
			HollowRegistryKeys.COPPER_INSTRUMENT,
			CODEC
	);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<CopperInstrument>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(
			HollowRegistryKeys.COPPER_INSTRUMENT,
			PACKET_CODEC
	);
}
