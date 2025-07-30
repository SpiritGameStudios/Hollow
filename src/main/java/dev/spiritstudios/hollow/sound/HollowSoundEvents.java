package dev.spiritstudios.hollow.sound;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class HollowSoundEvents {
    public static final RegistryEntry.Reference<SoundEvent> MUSIC_DISC_POSTMORTEM = registerReference("music_disc.postmortem");
    public static final RegistryEntry.Reference<SoundEvent> MUSIC_OVERWORLD_BIRCH_FOREST = registerReference("music.overworld.birch_forest");

    public static final SoundEvent BLOCK_SCULK_JAW_BITE = register("block.sculk_jaw.bite");
    public static final SoundEvent BLOCK_STONE_CHEST_EXTRACT = register("block.stone_chest.extract");

    public static final SoundEvent ITEM_COPPER_HORN_CALL_GREAT = register("item.copper_horn.call.great");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_OLD = register("item.copper_horn.call.old");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_PURE = register("item.copper_horn.call.pure");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_HUMBLE = register("item.copper_horn.call.humble");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_DRY = register("item.copper_horn.call.dry");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_CLEAR = register("item.copper_horn.call.clear");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_FRESH = register("item.copper_horn.call.fresh");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_SECRET = register("item.copper_horn.call.secret");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_FEARLESS = register("item.copper_horn.call.fearless");
    public static final SoundEvent ITEM_COPPER_HORN_CALL_SWEET = register("item.copper_horn.call.sweet");

    public static final SoundEvent ITEM_COPPER_HORN_MELODY_SKY = register("item.copper_horn.melody.sky");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_HYMN = register("item.copper_horn.melody.hymn");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_WATER = register("item.copper_horn.melody.water");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_FIRE = register("item.copper_horn.melody.fire");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_URGE = register("item.copper_horn.melody.urge");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_TEMPER = register("item.copper_horn.melody.temper");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_NEST = register("item.copper_horn.melody.nest");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_LAKE = register("item.copper_horn.melody.lake");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_RIVER = register("item.copper_horn.melody.river");
    public static final SoundEvent ITEM_COPPER_HORN_MELODY_MOON = register("item.copper_horn.melody.moon");

    public static final SoundEvent ITEM_COPPER_HORN_BASS_FALLING = register("item.copper_horn.bass.falling");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_RESTING = register("item.copper_horn.bass.resting");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_DESIRE = register("item.copper_horn.bass.desire");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_MEMORY = register("item.copper_horn.bass.memory");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_ANGER = register("item.copper_horn.bass.anger");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_JOURNEY = register("item.copper_horn.bass.journey");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_THOUGHT = register("item.copper_horn.bass.thought");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_TEAR = register("item.copper_horn.bass.tear");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_GIFT = register("item.copper_horn.bass.gift");
    public static final SoundEvent ITEM_COPPER_HORN_BASS_LOVE = register("item.copper_horn.bass.love");

    private static SoundEvent register(String path) {
        Identifier id = Hollow.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String path) {
        Identifier id = Hollow.id(path);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        // NO-OP
    }
}
