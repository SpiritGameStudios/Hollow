package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.PolyporeBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootTableProvider extends FabricBlockLootSubProvider {

    protected LootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropWhenSilkTouch(HollowBlocks.ECHOING_POT);
        this.dropWhenSilkTouch(HollowBlocks.SCULK_JAW);

        this.dropWhenSilkTouch(HollowBlocks.STONE_CHEST_LID);
        this.add(HollowBlocks.STONE_CHEST, this.createNameableBlockEntityTable(HollowBlocks.STONE_CHEST));

        this.dropSelf(HollowBlocks.CATTAIL);
        this.dropOther(HollowBlocks.CATTAIL_STEM, HollowBlocks.CATTAIL);

        this.dropSelf(HollowBlocks.FLOWERING_LILY_PAD);
        this.dropSelf(HollowBlocks.GIANT_LILY_PAD);

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

        this.dropWhenSilkTouch(HollowBlocks.JAR_OF_FIREFLIES);
        this.add(
                HollowBlocks.JAR,
                this.createNameableBlockEntityTable(HollowBlocks.JAR)
                        .modifyPools(pool -> pool.when(this.hasSilkTouch()))
        );

        HollowBlocks.OAK_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.SPRUCE_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.BIRCH_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.JUNGLE_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.ACACIA_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.CHERRY_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.PALE_OAK_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.DARK_OAK_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.MANGROVE_HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.CRIMSON_HOLLOW_STEM.forEach(this::dropSelf);
        HollowBlocks.WARPED_HOLLOW_STEM.forEach(this::dropSelf);

        HollowBlocks.COPPER_PILLAR.forEach(this::dropSelf);
    }
}
