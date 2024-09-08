package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.SoundEventRegistrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class HollowSoundEventRegistrar implements SoundEventRegistrar {
    @Name("music_disc.postmortem")
    public static final SoundEvent MUSIC_DISC_POSTMORTEM = SoundEvent.of(Identifier.of(MODID, "music_disc.postmortem"));

    @Name("music.birch_forest")
    public static final SoundEvent MUSIC_BIRCH_FOREST = SoundEvent.of(Identifier.of(MODID, "music.birch_forest"));

    @Name("music.swamp")
    public static final SoundEvent MUSIC_SWAMP = SoundEvent.of(Identifier.of(MODID, "music.swamp"));

    @Name("music.deep_dark")
    public static final SoundEvent MUSIC_DEEP_DARK = SoundEvent.of(Identifier.of(MODID, "music.deep_dark"));

    @Name("block.sculk_jaw.bite")
    public static final SoundEvent SCULK_JAW_BITE = SoundEvent.of(Identifier.of(MODID, "block.sculk_jaw.bite"));
}
