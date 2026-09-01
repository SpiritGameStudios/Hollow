package dev.spiritstudios.hollow.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.entity.Entity;

public final class ClientPacketListenerEvents {
    public static final Event<AddEntitySoundInstance> ADD_ENTITY_SOUND_INSTANCE = EventFactory.createArrayBacked(AddEntitySoundInstance.class, callbacks -> (minecraft, soundManager, entity) -> {
        for (AddEntitySoundInstance callback : callbacks)
            callback.onAddEntitySoundInstance(minecraft, soundManager, entity);
    });

    @FunctionalInterface
    public interface AddEntitySoundInstance {
        void onAddEntitySoundInstance(Minecraft minecraft, SoundManager soundManager, Entity entity);
    }
}
