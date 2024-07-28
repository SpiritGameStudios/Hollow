package dev.callmeecho.hollow.datagen;

import dev.callmeecho.hollow.block.HollowLogBlock;
import dev.callmeecho.hollow.block.OxidizablePillarBlock;
import dev.callmeecho.hollow.registry.HollowBlockRegistrar;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        createHollowLogRecipe(exporter, HollowBlockRegistrar.OAK_HOLLOW_LOG, ItemTags.OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.SPRUCE_HOLLOW_LOG, ItemTags.SPRUCE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.BIRCH_HOLLOW_LOG, ItemTags.BIRCH_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.JUNGLE_HOLLOW_LOG, ItemTags.JUNGLE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.ACACIA_HOLLOW_LOG, ItemTags.ACACIA_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.DARK_OAK_HOLLOW_LOG, ItemTags.DARK_OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.CRIMSON_HOLLOW_STEM, ItemTags.CRIMSON_STEMS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.WARPED_HOLLOW_STEM, ItemTags.WARPED_STEMS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.MANGROVE_HOLLOW_LOG, ItemTags.MANGROVE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistrar.CHERRY_HOLLOW_LOG, ItemTags.CHERRY_LOGS);

        HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get().forEach(
                (unWaxed, waxed) -> {
                    if (!(unWaxed instanceof OxidizablePillarBlock)) return;

                    ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, waxed)
                            .input(unWaxed)
                            .input(Items.HONEYCOMB)
                            .group(getItemPath(waxed))
                            .criterion(hasItem(unWaxed), conditionsFromItem(unWaxed))
                            .offerTo(exporter, convertBetween(waxed, Items.HONEYCOMB));
                }
        );

        Map.of(
                HollowBlockRegistrar.COPPER_PILLAR, Blocks.CUT_COPPER_SLAB,
                HollowBlockRegistrar.EXPOSED_COPPER_PILLAR, Blocks.EXPOSED_CUT_COPPER_SLAB,
                HollowBlockRegistrar.WEATHERED_COPPER_PILLAR, Blocks.WEATHERED_CUT_COPPER_SLAB,
                HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR, Blocks.OXIDIZED_CUT_COPPER_SLAB,

                HollowBlockRegistrar.WAXED_COPPER_PILLAR, Blocks.WAXED_CUT_COPPER_SLAB,
                HollowBlockRegistrar.WAXED_EXPOSED_COPPER_PILLAR, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                HollowBlockRegistrar.WAXED_WEATHERED_COPPER_PILLAR, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                HollowBlockRegistrar.WAXED_OXIDIZED_COPPER_PILLAR, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
        ).forEach(
                (pillar, slab) -> ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, pillar)
                        .input('#', slab)
                        .pattern("##")
                        .criterion(slab.getTranslationKey(), conditionsFromItem(slab))
                        .offerTo(exporter)
        );
    }

    public void createHollowLogRecipe(RecipeExporter exporter, HollowLogBlock block, TagKey<Item> LogBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 8)
                .criterion("has_logs", FabricRecipeProvider.conditionsFromTag(LogBlock))
                .input('#', LogBlock)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .offerTo(exporter);
    }
}
