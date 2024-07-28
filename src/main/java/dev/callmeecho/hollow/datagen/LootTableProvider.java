package dev.callmeecho.hollow.datagen;

import dev.callmeecho.cabinetapi.util.ReflectionHelper;
import dev.callmeecho.hollow.block.HollowLogBlock;
import dev.callmeecho.hollow.registry.HollowBlockRegistrar;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

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
                HollowBlockRegistrar.class,
                HollowLogBlock.class,
                (block, name, field) -> biConsumer.accept(block.getLootTableKey(), this.drops(block, ConstantLootNumberProvider.create(1.0f))));

        List.of(
                HollowBlockRegistrar.COPPER_PILLAR,
                HollowBlockRegistrar.EXPOSED_COPPER_PILLAR,
                HollowBlockRegistrar.WEATHERED_COPPER_PILLAR,
                HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR,

                HollowBlockRegistrar.WAXED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_EXPOSED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_WEATHERED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_OXIDIZED_COPPER_PILLAR
        ).forEach(
                block -> biConsumer.accept(block.getLootTableKey(), this.drops(block, ConstantLootNumberProvider.create(1.0f)))
        );
    }
}
