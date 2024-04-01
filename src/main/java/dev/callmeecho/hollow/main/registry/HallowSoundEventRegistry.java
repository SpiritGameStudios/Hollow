package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static dev.callmeecho.hollow.main.Hollow.MODID;

public class HallowSoundEventRegistry implements Registrar<SoundEvent> {
    @Override
    public Registry<SoundEvent> getRegistry() {
        return Registries.SOUND_EVENT;
    }

    public static final SoundEvent PRENUPTIAL = SoundEvent.of(new Identifier(MODID, "prenuptial"));
}
