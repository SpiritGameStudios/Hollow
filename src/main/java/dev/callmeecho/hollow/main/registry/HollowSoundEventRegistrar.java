package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.cabinetapi.registry.SoundEventRegistrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static dev.callmeecho.hollow.main.Hollow.MODID;

public class HollowSoundEventRegistrar implements SoundEventRegistrar {
    @Name("music_disc.postmortem")
    public static final SoundEvent MUSIC_DISC_POSTMORTEM = SoundEvent.of(Identifier.of(MODID, "music_disc.postmortem"));

    @Name("music.birch_forest")
    public static final SoundEvent MUSIC_BIRCH_FOREST = SoundEvent.of(Identifier.of(MODID, "music.birch_forest"));

    @Name("music.swamp")
    public static final SoundEvent MUSIC_SWAMP = SoundEvent.of(Identifier.of(MODID, "music.swamp"));

    @Name("music.deep_dark")
    public static final SoundEvent MUSIC_DEEP_DARK = SoundEvent.of(Identifier.of(MODID, "music.deep_dark"));
}
