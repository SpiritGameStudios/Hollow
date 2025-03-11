package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin extends MultifaceGrowthBlock {
    public SculkVeinBlockMixin(Settings settings) {
        super(settings);
    }

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SculkVeinBlock;canGrowOn(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
    private static boolean canGrowOn(BlockView blockView, Direction direction, BlockPos blockPos, BlockState blockState, Operation<Boolean> original) {
        return original.call(blockView, direction, blockPos, blockState) && !blockState.isOf(HollowBlocks.SCULK_JAW);
    }

    @WrapOperation(
            method = "spreadAtSamePosition",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 1)
    )
    private boolean spreadAtSamePosition(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || original.call(instance, HollowBlocks.SCULK_JAW);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (neighborState.isOf(HollowBlocks.SCULK_JAW) && direction == Direction.DOWN) return Blocks.AIR.getDefaultState();
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
