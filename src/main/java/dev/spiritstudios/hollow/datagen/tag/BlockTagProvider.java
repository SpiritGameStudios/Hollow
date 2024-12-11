package dev.spiritstudios.hollow.datagen.tag;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        FabricTagProvider<Block>.FabricTagBuilder hollowLogs = getOrCreateTagBuilder(HollowBlocks.Tags.HOLLOW_LOGS);
        FabricTagProvider<Block>.FabricTagBuilder axeMineable = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE);

        ReflectionHelper.getStaticFields(
                HollowBlocks.class,
                HollowLogBlock.class
        ).forEach(pair -> {
            hollowLogs.add(pair.value());
            axeMineable.add(pair.value());
        });

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(HollowBlocks.ECHOING_POT)
                .add(HollowBlocks.STONE_CHEST)
                .add(HollowBlocks.STONE_CHEST_LID)
                .add(HollowBlocks.COPPER_PILLAR)
                .add(HollowBlocks.WEATHERED_COPPER_PILLAR)
                .add(HollowBlocks.EXPOSED_COPPER_PILLAR)
                .add(HollowBlocks.OXIDIZED_COPPER_PILLAR)
                .add(HollowBlocks.WAXED_COPPER_PILLAR)
                .add(HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR)
                .add(HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR)
                .add(HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR);

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(HollowBlocks.SCULK_JAW);

        getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                .add(HollowBlocks.POTTED_ROOTED_ORCHID)
                .add(HollowBlocks.POTTED_PAEONIA);

        getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .add(HollowBlocks.PAEONIA)
                .add(HollowBlocks.ROOTED_ORCHID)
                .add(HollowBlocks.LOTUS_LILYPAD);

        getOrCreateTagBuilder(BlockTags.TALL_FLOWERS)
                .add(HollowBlocks.CAMPION);

        getOrCreateTagBuilder(HollowBlocks.Tags.POLYPORE_PLACEABLE_ON)
                .forceAddTag(BlockTags.LOGS)
                .addTag(HollowBlocks.Tags.HOLLOW_LOGS);
    }
}
