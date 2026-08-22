package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(method = "isStateClimbable", at = @At("RETURN"))
	private boolean modifyIsClimbable(boolean original, BlockState state) {
		assert (Object) this instanceof Entity;
		return original || HollowLogBlock.isClimbableHollowLog(state, (Entity) (Object) this);
	}
}
