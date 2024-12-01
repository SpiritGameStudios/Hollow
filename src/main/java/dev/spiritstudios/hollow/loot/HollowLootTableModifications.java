package dev.spiritstudios.hollow.loot;

import dev.spiritstudios.hollow.registry.HollowItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public class HollowLootTableModifications {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;

            if (LootTables.ANCIENT_CITY_CHEST == key) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(HollowItems.MUSIC_DISC_POSTMORTEM))
                        .conditionally(RandomChanceLootCondition.builder(0.1F));

                tableBuilder.pool(lootPoolBuilder);
            }

            if (LootTables.PILLAGER_OUTPOST_CHEST == key) {
                LootPool.Builder lootPoolBuilder = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                        .with(ItemEntry.builder(HollowItems.COPPER_HORN))
                        .apply(SetCopperInstrumentFunction.builder());

                tableBuilder.pool(lootPoolBuilder);
            }
        });
    }
}
