package dev.callmeecho.hollow.datagen;

import dev.callmeecho.cabinetapi.util.ReflectionHelper;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class LootTableProvider extends FabricBlockLootTableProvider {

    protected LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() { }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> biConsumer) {
        ReflectionHelper.forEachStaticField(
                HollowBlockRegistry.class,
                HollowLogBlock.class,
                (block, name, field) -> biConsumer.accept(block.getLootTableKey(), this.drops(block, ConstantLootNumberProvider.create(1.0f))));

        List.of(
                HollowBlockRegistry.COPPER_PILLAR,
                HollowBlockRegistry.EXPOSED_COPPER_PILLAR,
                HollowBlockRegistry.WEATHERED_COPPER_PILLAR,
                HollowBlockRegistry.OXIDIZED_COPPER_PILLAR,

                HollowBlockRegistry.WAXED_COPPER_PILLAR,
                HollowBlockRegistry.WAXED_EXPOSED_COPPER_PILLAR,
                HollowBlockRegistry.WAXED_WEATHERED_COPPER_PILLAR,
                HollowBlockRegistry.WAXED_OXIDIZED_COPPER_PILLAR
        ).forEach(
                block -> biConsumer.accept(block.getLootTableKey(), this.drops(block, ConstantLootNumberProvider.create(1.0f)))
        );
    }
}
