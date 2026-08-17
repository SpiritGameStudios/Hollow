package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.world.level.block.SculkVeinBlock$SculkVeinSpreaderConfig")
public abstract class SculkVeinGrowCheckerMixin {
    @WrapMethod(
            method = "stateCanBeReplaced(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)Z"
    )
    private boolean canGrow(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState, Operation<Boolean> original) {
        return original.call(level, sourcePos, placementPos, placementDirection, existingState) && !level.getBlockState(sourcePos.below()).is(HollowBlocks.SCULK_JAW);
    }
}
