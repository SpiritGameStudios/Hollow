package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.world.level.block.FireflyBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireflyBushBlock.class)
public class FireflyBushBlockMixin {
	@WrapOperation(method = "lambda$performBonemeal$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FireflyBushBlock;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"))
	private BlockState setToNormalSwitchgrass(FireflyBushBlock instance, Operation<BlockState> original) {
		return original.call(HollowBlocks.SWITCHGRASS);
	}
}
