package dev.callmeecho.hollow;

import dev.callmeecho.hollow.registry.HollowItemRegistrar;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

public class HollowLootTableModifications {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (LootTables.ANCIENT_CITY_CHEST == key && source.isBuiltin()) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(HollowItemRegistrar.MUSIC_DISC_POSTMORTEM))
                        .conditionally(RandomChanceLootCondition.builder(0.1F));

                tableBuilder.pool(lootPoolBuilder);
            }
        });
    }
}
