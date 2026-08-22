package dev.spiritstudios.hollow.world.level;

import dev.spiritstudios.hollow.Hollow;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class HollowGameRules {
    public static final GameRule<Boolean> COPPER_BULB_DELAY = GameRuleBuilder.forBoolean(false)
            .category(GameRuleCategory.UPDATES)
            .buildAndRegister(Hollow.id("copper_bulb_delay"));

    public static void init() {
        // NO-OP
    }
}
