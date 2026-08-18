package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
    public PlayerMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected float maxStepUp(Operation<Float> original) {
        BlockPos effectPos = this.getOnPos(0.2F);
        BlockState effectState = this.level().getBlockState(effectPos);

        return effectState.is(HollowBlocks.SCULK_JAW) ? 0.0F : original.call();
    }
}
