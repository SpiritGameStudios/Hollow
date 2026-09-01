package dev.spiritstudios.hollow.client.mixin;

import dev.spiritstudios.hollow.client.model.object.boat.FurnaceBoatModel;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public class AbstractBoatRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;Lnet/minecraft/client/renderer/entity/state/BoatRenderState;F)V", at = @At("TAIL"))
	private void addFuelRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks, CallbackInfo ci) {
		if (entity instanceof AbstractFurnaceBoat furnaceBoat) {
			state.setData(FurnaceBoatModel.HAS_FUEL, furnaceBoat.hasFuel());
		}
	}
}
