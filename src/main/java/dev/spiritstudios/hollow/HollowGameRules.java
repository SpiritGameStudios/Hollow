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

    public static final GameRules.Key<GameRules.BooleanRule> COPPER_BULB_DELAY = GameRuleRegistry.register(
            "copperBulbDelay",
            GameRules.Category.UPDATES,
            GameRuleFactory.createBooleanRule(true)
    );

    public static void init() {
        // NO-OP
    }
}
