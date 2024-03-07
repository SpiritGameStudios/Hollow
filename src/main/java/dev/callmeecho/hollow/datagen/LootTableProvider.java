package dev.callmeecho.hollow.datagen;

import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class LootTableProvider extends FabricBlockLootTableProvider {
    protected LootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() { }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        RegistrarHandler.<HollowLogBlock>forEach(HollowBlockRegistry.class, (block) -> biConsumer.accept(block.getLootTableId(), this.drops(block, ConstantLootNumberProvider.create(1.0f))));
    }
}
