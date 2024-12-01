package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
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

        this.addDrop(HollowBlocks.CATTAIL_STEM, HollowBlocks.CATTAIL);
        this.addDrop(HollowBlocks.CATTAIL);

        this.addDrop(HollowBlocks.TWIG);

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
