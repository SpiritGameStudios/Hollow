package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.world.level.block.FireflyJarBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.PolyporeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HollowLootTableProvider extends FabricBlockLootSubProvider {
    public HollowLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropWhenSilkTouch(HollowBlocks.ECHOING_POT);
        this.dropWhenSilkTouch(HollowBlocks.SCULK_JAW);

        this.dropWhenSilkTouch(HollowBlocks.STONE_CHEST_LID);
        this.add(HollowBlocks.STONE_CHEST, this.createNameableBlockEntityTable(HollowBlocks.STONE_CHEST));

		this.dropSelf(HollowBlocks.SWITCHGRASS);
        this.dropSelf(HollowBlocks.CATTAIL);
        this.dropOther(HollowBlocks.CATTAIL_STEM, HollowBlocks.CATTAIL);

        this.dropSelf(HollowBlocks.FLOWERING_LILY_PAD);
		this.add(HollowBlocks.GIANT_LILY_PAD, this.createSingleItemTable(Items.LILY_PAD, ConstantValue.exactly(4.0F)));

        this.add(
                HollowBlocks.POLYPORE,
                block -> LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(this.applyExplosionDecay(HollowBlocks.POLYPORE, LootItem.lootTableItem(block).apply(
                                List.of(2, 3),
                                polypore ->
                                        SetItemCountFunction.setCount(ConstantValue.exactly(polypore))
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PolyporeBlock.POLYPORE_AMOUNT, polypore))
                                                )
                        ))))
        );

		this.add(
			HollowBlocks.FIREFLY_JAR,
			block -> this.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(block))
					.apply(SetComponentsFunction.setComponent(DataComponents.CUSTOM_NAME, Component.literal("jeb_"))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FireflyJarBlock.JEB, true)))
					)
				)
			)
		);

        this.add(HollowBlocks.GLASS_JAR, this::createNameableBlockEntityTable);

        HollowBlocks.HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.STRIPPED_HOLLOW_LOG.forEach(this::dropSelf);

        HollowBlocks.COPPER_PILLAR.forEach(this::dropSelf);
    }
}
