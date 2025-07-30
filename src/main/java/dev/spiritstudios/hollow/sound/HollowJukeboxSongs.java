package dev.spiritstudios.hollow.sound;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public final class HollowJukeboxSongs {
	public static final RegistryKey<JukeboxSong> POSTMORTEM = of("postmortem");

	public static void bootstrap(Registerable<JukeboxSong> registry) {
		register(registry, POSTMORTEM, HollowSoundEvents.MUSIC_DISC_POSTMORTEM, 84.0F, 8);
	}

	private static RegistryKey<JukeboxSong> of(String id) {
		return RegistryKey.of(RegistryKeys.JUKEBOX_SONG , Hollow.id(id));
	}

	private static void register(
			Registerable<JukeboxSong> registry, RegistryKey<JukeboxSong> key, RegistryEntry.Reference<SoundEvent> soundEvent, float lengthInSeconds, int comparatorOutput
	) {
		registry.register(
				key, new JukeboxSong(soundEvent, Text.translatable(Util.createTranslationKey("jukebox_song", key.getValue())), lengthInSeconds, comparatorOutput)
		);
	}
}
