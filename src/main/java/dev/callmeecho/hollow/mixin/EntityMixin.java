package dev.callmeecho.hollow.mixin;


import dev.callmeecho.hollow.main.block.SculkJawBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract World getWorld();

    @Shadow public abstract BlockPos getLandingPos();

    @Shadow protected abstract BlockPos getPosWithYOffset(float offset);

    @Redirect(method = "bypassesSteppingEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    private boolean isSneaking(Entity instance) {
        if (getWorld().getBlockState(getLandingPos()).getBlock() == HollowBlockRegistrar.SCULK_JAW) return false;
        return instance.isSneaking();
    }
    
    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
    private Vec3d move(Vec3d movement, MovementType type) {
        BlockState block = getWorld().getBlockState(getPosWithYOffset(0));
        if (type == MovementType.SELF && block.getBlock() == HollowBlockRegistrar.SCULK_JAW && block.get(SculkJawBlock.ACTIVE)) {
            return Vec3d.ZERO;
        }
        return movement;
    }
}

