package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.block.EchoingVaseBlock;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class DireCurseMixin extends ServerCommonNetworkHandler {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public abstract ServerPlayerEntity getPlayer();

    public DireCurseMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Inject(
            method = "method_44900(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;Ljava/util/Optional;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;filterText(Ljava/lang/String;)Ljava/util/concurrent/CompletableFuture;"),
            cancellable = true
    )
    public void invoke(ChatMessageC2SPacket chatMessageC2SPacket, Optional<LastSeenMessageList> optional, CallbackInfo ci, @Local(ordinal = 0) SignedMessage signedMessage) {
        String message = signedMessage.getSignedContent();
        if (EchoingVaseBlock.ObaboBlock.DIRE_CURSE.matcher(message).find()) {
            this.server.execute(() -> {
                EchoingVaseBlock.ObaboBlock.invokeCurse(this.player);
            });
            ci.cancel();
        }
    }
}
