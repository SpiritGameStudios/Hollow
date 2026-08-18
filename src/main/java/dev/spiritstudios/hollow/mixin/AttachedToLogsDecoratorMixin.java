package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttachedToLogsDecorator.class)
public class AttachedToLogsDecoratorMixin {
    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/TreeDecorator$Context;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"))
    private void setHollowLogLayer(
            TreeDecorator.Context context, BlockPos pos, BlockState state,
            Operation<Void> original,
            @Local(name = "logsPos") BlockPos logsPos
    ) {
        original.call(context, pos, state);

        HollowLogBlock.Layer layer = HollowLogBlock.Layer.get(state);

        if (layer != HollowLogBlock.Layer.NONE) {
            BlockState log = context.level().getBlockState(logsPos);

            if (log.hasProperty(HollowLogBlock.LAYER)) {
                context.setBlock(logsPos, log.setValue(HollowLogBlock.LAYER, layer));
            }
        }
    }
}
