package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapOperation(
            method = "move",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V")
    )
    public void actuallyOnLilypad(Block instance, World world, BlockPos pos, BlockState state, Entity entity, Operation<Void> original) {
        if (instance == Blocks.WATER && world.getBlockState(pos.up()).isOf(HollowBlocks.SUPER_GIANT_LILYPAD)) {
            HollowBlocks.SUPER_GIANT_LILYPAD.onSteppedOn(world, pos, state, entity);
        } else original.call(instance, world, pos, state, entity);
    }
}
