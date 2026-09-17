package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.concurrent.CompletableFuture;

public class HollowBlockLootProvider extends FabricBlockLootSubProvider {
    public HollowBlockLootProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.dropWhenSilkTouch(HollowBlocks.ECHOING_POT);
        this.dropWhenSilkTouch(HollowBlocks.SCULK_JAW);

        this.dropWhenSilkTouch(HollowBlocks.STONE_CHEST_LID);
        this.add(HollowBlocks.STONE_CHEST, this::createNameableBlockEntityTable);

		this.dropSelf(HollowBlocks.SWITCHGRASS);
		this.add(HollowBlocks.CATTAIL, this::createShearsOrSilkTouchOnlyDrop);

		this.dropSelf(HollowBlocks.FLOWERING_LILY_PAD);
		this.add(HollowBlocks.GIANT_LILY_PAD, this.createSingleItemTable(Items.LILY_PAD, ContextIntProviders.exactly(4)));

		this.add(HollowBlocks.FIREFLY_JAR, this::createNameableBlockEntityTable);
        this.add(HollowBlocks.GLASS_JAR, this::createNameableBlockEntityTable);

        HollowBlocks.HOLLOW_LOG.forEach(this::dropSelf);
        HollowBlocks.STRIPPED_HOLLOW_LOG.forEach(this::dropSelf);

        HollowBlocks.COPPER_PILLAR.forEach(this::dropSelf);
    }
}
