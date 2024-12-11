package dev.spiritstudios.hollow.mixin;

import dev.spiritstudios.hollow.HollowGameRules;
import dev.spiritstudios.hollow.registry.HollowCriteria;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.task.FrogEatEntityTask;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(FrogEatEntityTask.class)
public abstract class FrogEatEntityTaskMixin {
    @Inject(method = "eat", at = @At("HEAD"))
    private void eat(ServerWorld world, FrogEntity frog, CallbackInfo ci) {
        if (!world.getGameRules().getBoolean(HollowGameRules.DO_FROG_POISONING)) return;

        Optional<Entity> target = frog.getFrogTarget();
        if (target.isEmpty()) return;
        if (!target.get().getType().isIn(HollowEntityTypes.Tags.POISONS_FROG) || world.random.nextFloat() <= 0.75F) return;

        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(
                StatusEffects.POISON,
                100,
                0
        );

        frog.addStatusEffect(statusEffectInstance);
        PlayerLookup.tracking(frog).forEach(HollowCriteria.FROG_POISONED::trigger);
    }
}
