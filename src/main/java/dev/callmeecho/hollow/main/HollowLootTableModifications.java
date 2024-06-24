package dev.callmeecho.hollow.main;

import dev.callmeecho.hollow.main.registry.HollowItemRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

public class HollowLootTableModifications {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            if (LootTables.ANCIENT_CITY_CHEST == key && source.isBuiltin()) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(HollowItemRegistry.MUSIC_DISC_POSTMORTEM))
                        .conditionally(RandomChanceLootCondition.builder(0.1F));

                tableBuilder.pool(lootPoolBuilder);
            }
        });
    }
}
