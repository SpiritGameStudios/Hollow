package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.specter.api.block.BlockMetatags;
import dev.spiritstudios.specter.api.block.FlammableBlockData;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BlockMetatagProvider extends MetatagProvider<Block> {
    protected BlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK);
    }

    @Override
    protected void configure(Consumer<MetatagBuilder<Block, ?>> provider, RegistryWrapper.WrapperLookup lookup) {
        MetatagBuilder<Block, FlammableBlockData> flammableBuilder = create(BlockMetatags.FLAMMABLE);

        ReflectionHelper.forEachStaticField(
                HollowBlockRegistrar.class,
                HollowLogBlock.class,
                (block, name, field) -> {
                    flammableBuilder.put(block, new FlammableBlockData(5, 5));
                });

        provider.accept(flammableBuilder);

        provider.accept(create(BlockMetatags.WAXABLE)
                .put(HollowBlockRegistrar.COPPER_PILLAR, HollowBlockRegistrar.WAXED_COPPER_PILLAR)
                .put(HollowBlockRegistrar.EXPOSED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_EXPOSED_COPPER_PILLAR)
                .put(HollowBlockRegistrar.WEATHERED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_WEATHERED_COPPER_PILLAR)
                .put(HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_OXIDIZED_COPPER_PILLAR));

        provider.accept(create(BlockMetatags.STRIPPABLE)
                .put(HollowBlockRegistrar.OAK_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_OAK_HOLLOW_LOG)
                .put(HollowBlockRegistrar.SPRUCE_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_SPRUCE_HOLLOW_LOG)
                .put(HollowBlockRegistrar.BIRCH_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_BIRCH_HOLLOW_LOG)
                .put(HollowBlockRegistrar.JUNGLE_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_JUNGLE_HOLLOW_LOG)
                .put(HollowBlockRegistrar.ACACIA_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_ACACIA_HOLLOW_LOG)
                .put(HollowBlockRegistrar.DARK_OAK_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_DARK_OAK_HOLLOW_LOG)
                .put(HollowBlockRegistrar.CRIMSON_HOLLOW_STEM, HollowBlockRegistrar.STRIPPED_CRIMSON_HOLLOW_STEM)
                .put(HollowBlockRegistrar.WARPED_HOLLOW_STEM, HollowBlockRegistrar.STRIPPED_WARPED_HOLLOW_STEM)
                .put(HollowBlockRegistrar.MANGROVE_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_MANGROVE_HOLLOW_LOG)
                .put(HollowBlockRegistrar.CHERRY_HOLLOW_LOG, HollowBlockRegistrar.STRIPPED_CHERRY_HOLLOW_LOG));

        provider.accept(create(BlockMetatags.OXIDIZABLE)
                .put(HollowBlockRegistrar.COPPER_PILLAR, HollowBlockRegistrar.EXPOSED_COPPER_PILLAR)
                .put(HollowBlockRegistrar.EXPOSED_COPPER_PILLAR, HollowBlockRegistrar.WEATHERED_COPPER_PILLAR)
                .put(HollowBlockRegistrar.WEATHERED_COPPER_PILLAR, HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR));
    }
}
