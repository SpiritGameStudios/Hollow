package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBoat.class)
public class AbstractBoatMixin {
	@ModifyExpressionValue(method = "controlBoat", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;inputUp:Z", ordinal = 1, opcode = Opcodes.GETFIELD))
	private boolean cancelManualForwardMovementIfFurnaceHasFuelLol(boolean original) {
		if ((Object) this instanceof AbstractFurnaceBoat furnaceBoat)
			return original && !furnaceBoat.isPropelled();

		return original;
	}
}
