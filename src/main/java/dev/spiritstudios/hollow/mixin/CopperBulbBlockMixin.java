package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.HollowGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CopperBulbBlock.class)
public abstract class CopperBulbBlockMixin extends Block {
    @Shadow public abstract void checkAndFlip(BlockState state, ServerLevel world, BlockPos pos);

    public CopperBulbBlockMixin(Properties settings) {
        super(settings);
    }

    @WrapOperation(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CopperBulbBlock;checkAndFlip(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V"))
    private void updateRedirect(CopperBulbBlock instance, BlockState state, ServerLevel world, BlockPos pos, Operation<Void> original) {
        if (world.getGameRules().get(HollowGameRules.COPPER_BULB_DELAY)) {
            world.scheduleTick(pos, instance, 1);
        } else {
            original.call(instance, state, world, pos);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        checkAndFlip(state, world, pos);
    }
}
