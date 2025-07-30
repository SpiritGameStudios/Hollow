package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.HollowGameRules;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BulbBlock.class)
public abstract class BulbBlockMixin extends Block {
    @Shadow public abstract void update(BlockState state, ServerWorld world, BlockPos pos);

    public BulbBlockMixin(Settings settings) {
        super(settings);
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BulbBlock;update(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    private void updateRedirect(BulbBlock instance, BlockState state, ServerWorld world, BlockPos pos, Operation<Void> original) {
        if (world.getGameRules().getBoolean(HollowGameRules.COPPER_BULB_DELAY))
            world.scheduleBlockTick(pos, instance, 1);
        else
            original.call(instance, state, world, pos);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        update(state, world, pos);
    }
}
