package dev.spiritstudios.hollow;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public final class HollowGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> DO_FROG_POISONING = GameRuleRegistry.register(
            "doFrogPoisoning",
            GameRules.Category.MOBS,
            GameRuleFactory.createBooleanRule(true)
    );

    public static void init() {
        // NO-OP
    }
}
