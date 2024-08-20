package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {
    @Redirect(method = "getExtraBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;", ordinal = 1))
    private BlockState getExtraBlockState(Block instance, @Local(argsOnly = true) Random random) {
        return random.nextFloat() > 0.35F ? HollowBlockRegistrar.SCULK_JAW.getDefaultState() : instance.getDefaultState();
    }
    
    @WrapOperation(method = "shouldNotDecay", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 1))
    private static boolean shouldNotDecay(BlockState state, Block block, Operation<Boolean> original) {
        return state.isOf(HollowBlockRegistrar.SCULK_JAW) || original.call(state, block);
    }
    
    @WrapOperation(method = "spread", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean spread(WorldAccess instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
       return original.call(instance, state.isOf(HollowBlockRegistrar.SCULK_JAW) ? pos.down() : pos, state, i);
    }
}
