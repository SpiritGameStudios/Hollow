package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;

public final class HollowJukeboxSongs {
	public static final ResourceKey<JukeboxSong> POSTMORTEM = create("postmortem");
	public static final ResourceKey<JukeboxSong> ONLY_YOU = create("only_you");

	public static void bootstrap(BootstrapContext<JukeboxSong> registry) {
		register(registry, POSTMORTEM, HollowSoundEvents.MUSIC_DISC_POSTMORTEM, 84, 8);
		register(registry, ONLY_YOU, HollowSoundEvents.MUSIC_DISC_ONLY_YOU, 183, 7);
	}

	private static ResourceKey<JukeboxSong> create(final String id) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, Hollow.id(id));
	}

	private static void register(
		BootstrapContext<JukeboxSong> context,
		ResourceKey<JukeboxSong> registryKey,
		Holder.Reference<SoundEvent> soundEvent,
		int lengthInSeconds,
		int comparatorOutput
	) {
		context.register(
			registryKey,
			new JukeboxSong(soundEvent, Component.translatable(Util.makeDescriptionId("jukebox_song", registryKey.identifier())), lengthInSeconds, comparatorOutput)
		);
	}
}
