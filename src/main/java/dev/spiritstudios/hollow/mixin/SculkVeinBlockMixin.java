package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin extends MultifaceSpreadeableBlock {
    public SculkVeinBlockMixin(Properties settings) {
        super(settings);
    }

    @WrapOperation(method = "regrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SculkVeinBlock;canAttachTo(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private static boolean canGrowOn(BlockGetter blockView, BlockPos blockPos, Direction direction, Operation<Boolean> original) {
        BlockState blockState = blockView.getBlockState(blockPos);
        return original.call(blockView, blockPos, direction) && !blockState.is(HollowBlocks.SCULK_JAW);
    }

    @WrapOperation(
            method = "onDischarged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z", ordinal = 1)
    )
    private boolean spreadAtSamePosition(BlockState instance, Object o, Operation<Boolean> original) {
        return original.call(instance, o) || original.call(instance, HollowBlocks.SCULK_JAW);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (neighborState.is(HollowBlocks.SCULK_JAW) && direction == Direction.DOWN) return Blocks.AIR.defaultBlockState();

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }
}
