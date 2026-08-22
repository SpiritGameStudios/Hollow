package dev.spiritstudios.hollow.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public record CopperInstrument(
        Holder<SoundEvent> call,
        Holder<SoundEvent> melody,
        Holder<SoundEvent> bass,
        float useDuration, float range,
        Component description
) {
	public static CopperInstrument of(String namespace, String call, String melody, String bass, float useDuration, float range) {
		return new CopperInstrument(
				BuiltInRegistries.SOUND_EVENT.get(Identifier.fromNamespaceAndPath(namespace, "item.copper_horn.call." + call)).orElseThrow(),
				BuiltInRegistries.SOUND_EVENT.get(Identifier.fromNamespaceAndPath(namespace, "item.copper_horn.melody." + melody)).orElseThrow(),
				BuiltInRegistries.SOUND_EVENT.get(Identifier.fromNamespaceAndPath(namespace, "item.copper_horn.bass." + bass)).orElseThrow(),
				useDuration, range,
				Component.translatable("item.hollow.copper_horn." + namespace + "." + call + "_" + melody + "_" + bass)
		);
	}

	public static final Codec<CopperInstrument> DIRECT_CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							SoundEvent.CODEC.fieldOf("call").forGetter(CopperInstrument::call),
							SoundEvent.CODEC.fieldOf("melody").forGetter(CopperInstrument::melody),
							SoundEvent.CODEC.fieldOf("bass").forGetter(CopperInstrument::bass),
							ExtraCodecs.POSITIVE_FLOAT.fieldOf("use_duration").forGetter(CopperInstrument::useDuration),
							ExtraCodecs.POSITIVE_FLOAT.fieldOf("range").forGetter(CopperInstrument::range),
							ComponentSerialization.CODEC.fieldOf("description").forGetter(CopperInstrument::description)
					)
					.apply(instance, CopperInstrument::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, CopperInstrument> DIRECT_STREAM_CODEC = StreamCodec.composite(
			SoundEvent.STREAM_CODEC, CopperInstrument::call,
			SoundEvent.STREAM_CODEC, CopperInstrument::melody,
			SoundEvent.STREAM_CODEC, CopperInstrument::bass,
			ByteBufCodecs.FLOAT, CopperInstrument::useDuration,
			ByteBufCodecs.FLOAT, CopperInstrument::range,
			ComponentSerialization.STREAM_CODEC, CopperInstrument::description,
			CopperInstrument::new
	);

	public static final Codec<Holder<CopperInstrument>> CODEC = RegistryFileCodec.create(
			HollowRegistries.COPPER_INSTRUMENT,
			DIRECT_CODEC
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CopperInstrument>> STREAM_CODEC = ByteBufCodecs.holder(
			HollowRegistries.COPPER_INSTRUMENT,
			DIRECT_STREAM_CODEC
	);
}
