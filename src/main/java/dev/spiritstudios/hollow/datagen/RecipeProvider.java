package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.OxidizablePillarBlock;
import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.item.datagen.SpecterShapedRecipeJsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        createHollowLogRecipe(exporter, HollowBlocks.OAK_HOLLOW_LOG, ItemTags.OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.SPRUCE_HOLLOW_LOG, ItemTags.SPRUCE_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.BIRCH_HOLLOW_LOG, ItemTags.BIRCH_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.JUNGLE_HOLLOW_LOG, ItemTags.JUNGLE_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.ACACIA_HOLLOW_LOG, ItemTags.ACACIA_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.DARK_OAK_HOLLOW_LOG, ItemTags.DARK_OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.CRIMSON_HOLLOW_STEM, ItemTags.CRIMSON_STEMS);
        createHollowLogRecipe(exporter, HollowBlocks.WARPED_HOLLOW_STEM, ItemTags.WARPED_STEMS);
        createHollowLogRecipe(exporter, HollowBlocks.MANGROVE_HOLLOW_LOG, ItemTags.MANGROVE_LOGS);
        createHollowLogRecipe(exporter, HollowBlocks.CHERRY_HOLLOW_LOG, ItemTags.CHERRY_LOGS);

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

        createCopperHornRecipe(exporter, Instruments.PONDER_GOAT_HORN, CopperInstrument.GREAT_SKY_FALLING);
        createCopperHornRecipe(exporter, Instruments.SING_GOAT_HORN, CopperInstrument.OLD_HYMN_RESTING);
        createCopperHornRecipe(exporter, Instruments.SEEK_GOAT_HORN, CopperInstrument.PURE_WATER_DESIRE);
        createCopperHornRecipe(exporter, Instruments.FEEL_GOAT_HORN, CopperInstrument.HUMBLE_FIRE_MEMORY);
        createCopperHornRecipe(exporter, Instruments.ADMIRE_GOAT_HORN, CopperInstrument.DRY_URGE_ANGER);
        createCopperHornRecipe(exporter, Instruments.CALL_GOAT_HORN, CopperInstrument.CLEAR_TEMPER_JOURNEY);
        createCopperHornRecipe(exporter, Instruments.YEARN_GOAT_HORN, CopperInstrument.FRESH_NEST_THOUGHT);
        createCopperHornRecipe(exporter, Instruments.DREAM_GOAT_HORN, CopperInstrument.SECRET_LAKE_TEAR);

        Map.of(
                HollowBlocks.COPPER_PILLAR, Blocks.CUT_COPPER_SLAB,
                HollowBlocks.EXPOSED_COPPER_PILLAR, Blocks.EXPOSED_CUT_COPPER_SLAB,
                HollowBlocks.WEATHERED_COPPER_PILLAR, Blocks.WEATHERED_CUT_COPPER_SLAB,
                HollowBlocks.OXIDIZED_COPPER_PILLAR, Blocks.OXIDIZED_CUT_COPPER_SLAB,

                HollowBlocks.WAXED_COPPER_PILLAR, Blocks.WAXED_CUT_COPPER_SLAB,
                HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB,
                HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB,
                HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
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

    public void createCopperHornRecipe(RecipeExporter exporter, RegistryKey<Instrument> goat, CopperInstrument copper) {
        SpecterShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, new ItemStack(
                        Registries.ITEM.getEntry(HollowItems.COPPER_HORN),
                        1,
                        ComponentChanges.builder()
                                .add(HollowDataComponentTypes.COPPER_INSTRUMENT, copper)
                                .build()
                ))
                .group("hollow_copper_horn")
                .input('#', Ingredient.ofItems(Items.COPPER_INGOT))
                .input('G', DefaultCustomIngredients.components(
                        Ingredient.ofItems(Items.GOAT_HORN),
                        ComponentChanges.builder()
                                .add(DataComponentTypes.INSTRUMENT, Registries.INSTRUMENT.entryOf(goat))
                                .build()
                ))
                .pattern("#G#")
                .pattern(" # ")
                .criterion("has_goat_horn", FabricRecipeProvider.conditionsFromItem(Items.GOAT_HORN))
                .offerTo(exporter, Identifier.of(MODID, "copper_horn" + copper.asString()));
    }
}
