package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {
    @WrapOperation(method = "getRandomGrowthState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1))
    private BlockState getExtraBlockState(Block instance, Operation<BlockState> original, @Local(argsOnly = true, name = "random") RandomSource random) {
        return random.nextFloat() < 0.35F ? HollowBlocks.SCULK_JAW.defaultBlockState() : original.call(instance);
    }
    
    @WrapOperation(method = "canPlaceGrowth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z", ordinal = 1))
    private static boolean shouldNotDecay(BlockState instance, Object o, Operation<Boolean> original) {
        return original.call(instance, HollowBlocks.SCULK_JAW) || original.call(instance, o);
    }
    
    @WrapOperation(method = "attemptUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean spread(LevelAccessor instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
       return original.call(instance, state.is(HollowBlocks.SCULK_JAW) ? pos.below() : pos, state, i);
    }
}
