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

            if (key == LootTables.ANCIENT_CITY_CHEST) tableBuilder.pool(LootPool.builder()
                    .with(ItemEntry.builder(HollowItems.MUSIC_DISC_POSTMORTEM))
                    .conditionally(RandomChanceLootCondition.builder(0.1F)));

            if (key == LootTables.PILLAGER_OUTPOST_CHEST) tableBuilder.pool(LootPool.builder()
                    .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                    .with(ItemEntry.builder(HollowItems.COPPER_HORN))
                    .apply(SetCopperInstrumentFunction.builder()));
        });
    }
}
