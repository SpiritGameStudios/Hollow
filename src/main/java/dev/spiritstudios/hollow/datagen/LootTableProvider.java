package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootTableProvider extends FabricBlockLootTableProvider {

    protected LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDropWithSilkTouch(HollowBlocks.ECHOING_POT);
        this.addDropWithSilkTouch(HollowBlocks.SCULK_JAW);
        this.addDropWithSilkTouch(HollowBlocks.STONE_CHEST_LID);
        this.addDrop(HollowBlocks.STONE_CHEST, this.nameableContainerDrops(HollowBlocks.STONE_CHEST));

        this.addDrop(HollowBlocks.CATTAIL_STEM, HollowBlocks.CATTAIL);
        this.addDrop(HollowBlocks.TWIG);
        this.addDrop(HollowBlocks.LOTUS_LILYPAD);
        this.addDrop(HollowBlocks.GIANT_LILYPAD);
        this.addDrop(HollowBlocks.CAMPION, block -> this.dropsWithProperty(block, TallPlantBlock.HALF, DoubleBlockHalf.LOWER));
        this.addDrop(HollowBlocks.PURPLE_WILDFLOWER, this.flowerbedDrops(HollowBlocks.PURPLE_WILDFLOWER));
        this.addDrop(HollowBlocks.WHITE_WILDFLOWER, this.flowerbedDrops(HollowBlocks.WHITE_WILDFLOWER));
        this.addDrop(HollowBlocks.BLUE_WILDFLOWER, this.flowerbedDrops(HollowBlocks.BLUE_WILDFLOWER));
        this.addDrop(HollowBlocks.PINK_WILDFLOWER, this.flowerbedDrops(HollowBlocks.PINK_WILDFLOWER));

        this.addDrop(HollowBlocks.PAEONIA);
        this.addPottedPlantDrops(HollowBlocks.POTTED_PAEONIA);
        this.addDrop(HollowBlocks.ROOTED_ORCHID);
        this.addPottedPlantDrops(HollowBlocks.POTTED_ROOTED_ORCHID);

        this.addDrop(
                HollowBlocks.POLYPORE,
                block -> LootTable.builder().pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(this.applyExplosionDecay(HollowBlocks.POLYPORE, ItemEntry.builder(block).apply(
                                List.of(2, 3),
                                polypore ->
                                        SetCountLootFunction.builder(ConstantLootNumberProvider.create(polypore))
                                                .conditionally(
                                                        BlockStatePropertyLootCondition.builder(block)
                                                                .properties(StatePredicate.Builder.create().exactMatch(PolyporeBlock.POLYPORE_AMOUNT, polypore))
                                                )
                        ))))
        );

        this.addDrop(HollowBlocks.FIREFLY_JAR);
        this.addDrop(HollowBlocks.JAR, this.nameableContainerDrops(HollowBlocks.JAR));


        ReflectionHelper.forEachStaticField(
                HollowBlocks.class,
                HollowLogBlock.class,
                (block, name, field) -> this.addDrop(block));

        List.of(
                HollowBlocks.COPPER_PILLAR,
                HollowBlocks.EXPOSED_COPPER_PILLAR,
                HollowBlocks.WEATHERED_COPPER_PILLAR,
                HollowBlocks.OXIDIZED_COPPER_PILLAR,

                HollowBlocks.WAXED_COPPER_PILLAR,
                HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR,
                HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR,
                HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR
        ).forEach(this::addDrop);


    }
}
