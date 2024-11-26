package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
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
        this.addDropWithSilkTouch(HollowBlockRegistrar.ECHOING_POT);
        this.addDropWithSilkTouch(HollowBlockRegistrar.SCULK_JAW);
        this.addDropWithSilkTouch(HollowBlockRegistrar.STONE_CHEST_LID);

        this.addDrop(HollowBlockRegistrar.CATTAIL_STEM, HollowBlockRegistrar.CATTAIL);
        this.addDrop(HollowBlockRegistrar.CATTAIL);

        this.addDrop(HollowBlockRegistrar.TWIG);

        ReflectionHelper.forEachStaticField(
                HollowBlockRegistrar.class,
                HollowLogBlock.class,
                (block, name, field) -> this.addDrop(block));

        List.of(
                HollowBlockRegistrar.COPPER_PILLAR,
                HollowBlockRegistrar.EXPOSED_COPPER_PILLAR,
                HollowBlockRegistrar.WEATHERED_COPPER_PILLAR,
                HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR,

                HollowBlockRegistrar.WAXED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_EXPOSED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_WEATHERED_COPPER_PILLAR,
                HollowBlockRegistrar.WAXED_OXIDIZED_COPPER_PILLAR
        ).forEach(this::addDrop);



    }
}
