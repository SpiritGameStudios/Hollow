package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.OxidizablePillarBlock;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.hollow.registry.HollowDataComponentRegistrar;
import dev.spiritstudios.hollow.registry.HollowItemRegistrar;
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

        createCopperHornRecipe(exporter, Instruments.PONDER_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.GREAT_SKY_FALLING);
        createCopperHornRecipe(exporter, Instruments.SING_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.OLD_HYMN_RESTING);
        createCopperHornRecipe(exporter, Instruments.SEEK_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.PURE_WATER_DESIRE);
        createCopperHornRecipe(exporter, Instruments.FEEL_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.HUMBLE_FIRE_MEMORY);
        createCopperHornRecipe(exporter, Instruments.ADMIRE_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.DRY_URGE_ANGER);
        createCopperHornRecipe(exporter, Instruments.CALL_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.CLEAR_TEMPER_JOURNEY);
        createCopperHornRecipe(exporter, Instruments.YEARN_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.FRESH_NEST_THOUGHT);
        createCopperHornRecipe(exporter, Instruments.DREAM_GOAT_HORN, HollowDataComponentRegistrar.CopperInstrument.SECRET_LAKE_TEAR);

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

    public void createCopperHornRecipe(RecipeExporter exporter, RegistryKey<Instrument> goat, HollowDataComponentRegistrar.CopperInstrument copper) {
        SpecterShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, new ItemStack(
                        Registries.ITEM.getEntry(HollowItemRegistrar.COPPER_HORN),
                        1,
                        ComponentChanges.builder()
                                .add(HollowDataComponentRegistrar.COPPER_INSTRUMENT, copper)
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
