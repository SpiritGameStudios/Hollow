package dev.spiritstudios.hollow.client.mixin.event;

import dev.spiritstudios.hollow.client.event.ClientPacketListenerEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {
	protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
		super(minecraft, connection, commonListenerCookie);
	}

	@Inject(method = "postAddEntitySoundInstance", at = @At("RETURN"))
	private void postAddEntitySoundInstanceEventImpl(Entity entity, CallbackInfo ci) {
		ClientPacketListenerEvents.ADD_ENTITY_SOUND_INSTANCE.invoker().onAddEntitySoundInstance(this.minecraft, this.minecraft.getSoundManager(), entity);
	}
}
