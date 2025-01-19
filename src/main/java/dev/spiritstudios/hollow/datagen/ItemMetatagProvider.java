package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.item.ItemMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ItemMetatagProvider extends MetatagProvider<Item> {
    protected ItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK);
    }

    @Override
    protected void configure(Consumer<MetatagBuilder<Item, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
        consumer.accept(create(ItemMetatags.COMPOSTING_CHANCE)
                .put(HollowBlocks.ROOTED_ORCHID.asItem(), 0.65F)
                .put(HollowBlocks.PAEONIA.asItem(), 0.65F)
                .put(HollowBlocks.POLYPORE.asItem(), 0.65F)
                .put(HollowBlocks.CAMPION.asItem(), 0.65F)
                .put(HollowBlocks.CATTAIL.asItem(), 0.65F)
                .put(HollowBlocks.LOTUS_LILYPAD.asItem(), 0.75F)
                .put(HollowBlocks.GIANT_LILYPAD.asItem(), 0.85F));
    }

    @Override
    public String getName() {
        return super.getName() + " for minecraft:item";
    }
}
