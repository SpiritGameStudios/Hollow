package dev.spiritstudios.hollow.world.level.storage.loot;

import dev.spiritstudios.hollow.world.item.HollowItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class HollowLootTableModifications {
    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;

            if (key == BuiltInLootTables.ANCIENT_CITY) tableBuilder.withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(HollowItems.MUSIC_DISC_POSTMORTEM))
                    .when(LootItemRandomChanceCondition.randomChance(0.1F)));

			if (key == BuiltInLootTables.FARMER_GIFT) tableBuilder.withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(HollowItems.MUSIC_DISC_ONLY_YOU))
				.when(LootItemRandomChanceCondition.randomChance(0.1F)));

            if (key == BuiltInLootTables.PILLAGER_OUTPOST) tableBuilder.withPool(LootPool.lootPool()
                    .setRolls(UniformGenerator.between(0.0F, 1.0F))
                    .add(LootItem.lootTableItem(HollowItems.COPPER_HORN))
                    .apply(SetCopperInstrumentFunction.builder()));
        });
    }
}
