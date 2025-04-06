package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.specter.api.registry.annotations.Name;
import net.minecraft.sound.SoundEvent;

public final class HollowSoundEvents {
    @Name("music_disc.postmortem")
    public static final SoundEvent MUSIC_DISC_POSTMORTEM = SoundEvent.of(Hollow.id("music_disc.postmortem"));

    @Name("music.birch_forest")
    public static final SoundEvent MUSIC_BIRCH_FOREST = SoundEvent.of(Hollow.id("music.birch_forest"));

    @Name("music.swamp")
    public static final SoundEvent MUSIC_SWAMP = SoundEvent.of(Hollow.id("music.swamp"));

    @Name("music.deep_dark")
    public static final SoundEvent MUSIC_DEEP_DARK = SoundEvent.of(Hollow.id("music.deep_dark"));

    @Name("block.sculk_jaw.bite")
    public static final SoundEvent SCULK_JAW_BITE = SoundEvent.of(Hollow.id("block.sculk_jaw.bite"));

    @Name("block.stone_chest.extract")
    public static final SoundEvent STONE_CHEST_EXTRACT = SoundEvent.of(Hollow.id("block.stone_chest.extract"));

    @Name("block.boioioioing")
    public static final SoundEvent BOIOIOIOING = SoundEvent.of(Hollow.id("block.boioioioing"));
}
