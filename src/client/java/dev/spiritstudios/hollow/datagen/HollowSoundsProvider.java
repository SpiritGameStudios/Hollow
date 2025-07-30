package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.sound.HollowSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public final class HollowSoundsProvider extends FabricSoundsProvider {
	public HollowSoundsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, SoundExporter exporter) {
		exporter.add(
				HollowSoundEvents.MUSIC_DISC_POSTMORTEM,
				SoundTypeBuilder.of()
						.sound(ofFile("records/postmortem").stream(true))
						.category(SoundCategory.RECORDS)
		);

		exporter.add(
				HollowSoundEvents.MUSIC_OVERWORLD_BIRCH_FOREST,
				SoundTypeBuilder.of()
						.sound(ofFile("music/game/birch_forest/wildflower").stream(true).volume(0.4F))
						.sound(ofFile("music/game/birch_forest/floraison").stream(true).volume(0.4F))
						.category(SoundCategory.MUSIC)
		);

		exporter.add(
				HollowSoundEvents.BLOCK_STONE_CHEST_EXTRACT,
				SoundTypeBuilder.of(HollowSoundEvents.BLOCK_STONE_CHEST_EXTRACT)
						.sound(ofVanillaFile("block/decorated_pot/insert1").volume(0.9F))
						.sound(ofVanillaFile("block/decorated_pot/insert2").volume(0.9F))
						.sound(ofVanillaFile("block/decorated_pot/insert3").volume(0.9F))
						.sound(ofVanillaFile("block/decorated_pot/insert4").volume(0.9F))
						.category(SoundCategory.BLOCKS)
		);

		exporter.add(
				HollowSoundEvents.BLOCK_SCULK_JAW_BITE,
				SoundTypeBuilder.of(HollowSoundEvents.BLOCK_SCULK_JAW_BITE)
						.sound(ofVanillaFile("mob/goat/horn_break1").volume(0.9F))
						.sound(ofVanillaFile("mob/goat/horn_break2").volume(0.9F))
						.sound(ofVanillaFile("mob/goat/horn_break3").volume(0.9F))
						.sound(ofVanillaFile("mob/goat/horn_break4").volume(0.9F))
						.category(SoundCategory.BLOCKS)
		);

		// region Copper Horn
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_GREAT, "great");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_OLD, "old");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_PURE, "pure");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_HUMBLE, "humble");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_DRY, "dry");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_CLEAR, "clear");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_FRESH, "fresh");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_SECRET, "secret");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_FEARLESS, "fearless");
		addCopperHornCall(exporter, HollowSoundEvents.ITEM_COPPER_HORN_CALL_SWEET, "sweet");

		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_SKY, "sky");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_HYMN, "hymn");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_WATER, "water");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_FIRE, "fire");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_URGE, "urge");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_TEMPER, "temper");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_NEST, "nest");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_LAKE, "lake");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_RIVER, "river");
		addCopperHornMelody(exporter, HollowSoundEvents.ITEM_COPPER_HORN_MELODY_MOON, "moon");

		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_FALLING, "falling");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_RESTING, "resting");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_DESIRE, "desire");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_MEMORY, "memory");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_ANGER, "anger");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_JOURNEY, "journey");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_THOUGHT, "thought");
		addCopperHornBass(exporter, HollowSoundEvents.ITEM_COPPER_HORN_BASS_TEAR, "tear");

		exporter.add(
				HollowSoundEvents.ITEM_COPPER_HORN_BASS_GIFT,
				SoundTypeBuilder.of(HollowSoundEvents.ITEM_COPPER_HORN_BASS_GIFT)
						.sound(ofFile("item/copper_horn/bass/gift").pitch(1.07F))
		);

		exporter.add(
				HollowSoundEvents.ITEM_COPPER_HORN_BASS_LOVE,
				SoundTypeBuilder.of(HollowSoundEvents.ITEM_COPPER_HORN_BASS_LOVE)
						.sound(ofFile("item/copper_horn/bass/love").pitch(1.7F))
		);

		// endregion

		// Vanilla Additions
		exporter.add(
				SoundEvents.MUSIC_OVERWORLD_SWAMP,
				SoundTypeBuilder.of()
						.sound(ofFile("music/game/swamp/waterlily").stream(true).volume(0.4F))
		);

		exporter.add(
				SoundEvents.MUSIC_OVERWORLD_DEEP_DARK,
				SoundTypeBuilder.of()
						.sound(ofFile("music/game/echo").stream(true))
		);
	}

	private void addCopperHorn(SoundExporter exporter, SoundEvent event, String type, String name) {
		exporter.add(event, SoundTypeBuilder.of(event).sound(ofFile("item/copper_horn/" + type + "/" + name)));
	}

	private void addCopperHornCall(SoundExporter exporter, SoundEvent event, String name) {
		addCopperHorn(exporter, event, "call", name);
	}

	private void addCopperHornMelody(SoundExporter exporter, SoundEvent event, String name) {
		addCopperHorn(exporter, event, "melody", name);
	}

	private void addCopperHornBass(SoundExporter exporter, SoundEvent event, String name) {
		addCopperHorn(exporter, event, "bass", name);
	}

	private SoundTypeBuilder.EntryBuilder ofFile(String path) {
		return SoundTypeBuilder.EntryBuilder.ofFile(Hollow.id(path));
	}

	private SoundTypeBuilder.EntryBuilder ofVanillaFile(String path) {
		return SoundTypeBuilder.EntryBuilder.ofFile(Identifier.ofVanilla(path));
	}

	@Override
	public String getName() {
		return "Sound Events";
	}
}
