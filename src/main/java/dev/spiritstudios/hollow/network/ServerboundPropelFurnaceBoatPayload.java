package dev.spiritstudios.hollow.network;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record ServerboundPropelFurnaceBoatPayload(UUID furnaceBoat, boolean propelled) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, ServerboundPropelFurnaceBoatPayload> CODEC = StreamCodec.of(ServerboundPropelFurnaceBoatPayload::write, ServerboundPropelFurnaceBoatPayload::new);
	public static final Type<ServerboundPropelFurnaceBoatPayload> TYPE = new Type<>(Hollow.id("serverbound_propel_furnace_boat_v1"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public ServerboundPropelFurnaceBoatPayload(FriendlyByteBuf buf) {
		this(buf.readUUID(), buf.readBoolean());
	}

	private static void write(FriendlyByteBuf buf, ServerboundPropelFurnaceBoatPayload payload) {
		buf.writeUUID(payload.furnaceBoat);
		buf.writeBoolean(payload.propelled);
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<ServerboundPropelFurnaceBoatPayload> {
		@Override
		public void receive(ServerboundPropelFurnaceBoatPayload payload, ServerPlayNetworking.Context context) {
			if (context.player().level().getEntityInAnyDimension(payload.furnaceBoat) instanceof AbstractFurnaceBoat furnaceBoat) {
				furnaceBoat.setIsPropelled(payload.propelled);
			}
		}
	}
}
