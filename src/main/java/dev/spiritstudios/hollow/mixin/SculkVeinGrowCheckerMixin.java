package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.block.SculkVeinBlock$SculkVeinGrowChecker")
public abstract class SculkVeinGrowCheckerMixin {
    @WrapMethod(
            method = "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;)Z"
    )
    private boolean canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state, Operation<Boolean> original) {
        return original.call(world, pos, growPos, direction, state) && !world.getBlockState(pos.down()).isOf(HollowBlocks.SCULK_JAW);
    }
}
