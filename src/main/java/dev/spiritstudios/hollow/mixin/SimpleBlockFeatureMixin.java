package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.world.level.block.CattailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleBlockFeature.class)
public class SimpleBlockFeatureMixin {
	@WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean placeTripleTall(WorldGenLevel instance, BlockPos blockPos, BlockState blockState, int i, Operation<Boolean> original) {
		if (blockState.getBlock() instanceof CattailBlock) {
			return CattailBlock.placeAt(instance, blockState, blockPos, i);
		} else {
			return original.call(instance, blockPos, blockState, i);
		}
	}
}
