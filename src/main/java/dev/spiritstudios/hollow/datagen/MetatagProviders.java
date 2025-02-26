package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.block.BlockMetatags;
import dev.spiritstudios.specter.api.block.FlammableBlockData;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import dev.spiritstudios.specter.api.item.ItemMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MetatagProviders {
    public static void addAll(FabricDataGenerator.Pack pack) {
        pack.addProvider(MetatagProviders.BlockMetatagProvider::new);
        pack.addProvider(MetatagProviders.ItemMetatagProvider::new);
    }

    public static class BlockMetatagProvider extends MetatagProvider<Block> {
        protected BlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, RegistryKeys.BLOCK, registriesFuture, DataOutput.OutputType.DATA_PACK);
        }

        @Override
        protected void configure(Consumer<MetatagBuilder<Block, ?>> provider, RegistryWrapper.WrapperLookup lookup) {
            MetatagBuilder<Block, FlammableBlockData> flammableBuilder = create(BlockMetatags.FLAMMABLE);

            ReflectionHelper.getStaticFields(
                    HollowBlocks.class,
                    HollowLogBlock.class
            ).forEach(pair ->
                    flammableBuilder.put(pair.value(), new FlammableBlockData(5, 5)));

            provider.accept(flammableBuilder);

            provider.accept(create(BlockMetatags.WAXABLE)
                    .put(HollowBlocks.COPPER_PILLAR, HollowBlocks.WAXED_COPPER_PILLAR)
                    .put(HollowBlocks.EXPOSED_COPPER_PILLAR, HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR)
                    .put(HollowBlocks.WEATHERED_COPPER_PILLAR, HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR)
                    .put(HollowBlocks.OXIDIZED_COPPER_PILLAR, HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR));

            provider.accept(create(BlockMetatags.OXIDIZABLE)
                    .put(HollowBlocks.COPPER_PILLAR, HollowBlocks.EXPOSED_COPPER_PILLAR)
                    .put(HollowBlocks.EXPOSED_COPPER_PILLAR, HollowBlocks.WEATHERED_COPPER_PILLAR)
                    .put(HollowBlocks.WEATHERED_COPPER_PILLAR, HollowBlocks.OXIDIZED_COPPER_PILLAR));

            provider.accept(create(BlockMetatags.STRIPPABLE)
                    .put(HollowBlocks.OAK_HOLLOW_LOG, HollowBlocks.STRIPPED_OAK_HOLLOW_LOG)
                    .put(HollowBlocks.SPRUCE_HOLLOW_LOG, HollowBlocks.STRIPPED_SPRUCE_HOLLOW_LOG)
                    .put(HollowBlocks.BIRCH_HOLLOW_LOG, HollowBlocks.STRIPPED_BIRCH_HOLLOW_LOG)
                    .put(HollowBlocks.JUNGLE_HOLLOW_LOG, HollowBlocks.STRIPPED_JUNGLE_HOLLOW_LOG)
                    .put(HollowBlocks.ACACIA_HOLLOW_LOG, HollowBlocks.STRIPPED_ACACIA_HOLLOW_LOG)
                    .put(HollowBlocks.DARK_OAK_HOLLOW_LOG, HollowBlocks.STRIPPED_DARK_OAK_HOLLOW_LOG)
                    .put(HollowBlocks.CRIMSON_HOLLOW_STEM, HollowBlocks.STRIPPED_CRIMSON_HOLLOW_STEM)
                    .put(HollowBlocks.WARPED_HOLLOW_STEM, HollowBlocks.STRIPPED_WARPED_HOLLOW_STEM)
                    .put(HollowBlocks.MANGROVE_HOLLOW_LOG, HollowBlocks.STRIPPED_MANGROVE_HOLLOW_LOG)
                    .put(HollowBlocks.CHERRY_HOLLOW_LOG, HollowBlocks.STRIPPED_CHERRY_HOLLOW_LOG)
                    .put(HollowBlocks.PALE_OAK_HOLLOW_LOG, HollowBlocks.STRIPPED_PALE_OAK_HOLLOW_LOG));
        }

        @Override
        public String getName() {
            return super.getName() + " for minecraft:block";
        }
    }

    public static class ItemMetatagProvider extends MetatagProvider<Item> {
        protected ItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, RegistryKeys.ITEM, registriesFuture, DataOutput.OutputType.DATA_PACK);
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
}
