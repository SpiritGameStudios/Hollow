package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.OxidizablePillarBlock;
import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.item.datagen.SpecterShapedRecipeJsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Instrument;
import net.minecraft.item.Instruments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);

        this.registriesFuture = registriesFuture;
    }

    @Override
    public void generate(RecipeExporter exporter) {
        createHollowLogRecipe(HollowBlocks.OAK_HOLLOW_LOG, ItemTags.OAK_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.SPRUCE_HOLLOW_LOG, ItemTags.SPRUCE_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.BIRCH_HOLLOW_LOG, ItemTags.BIRCH_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.JUNGLE_HOLLOW_LOG, ItemTags.JUNGLE_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.ACACIA_HOLLOW_LOG, ItemTags.ACACIA_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.DARK_OAK_HOLLOW_LOG, ItemTags.DARK_OAK_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.CRIMSON_HOLLOW_STEM, ItemTags.CRIMSON_STEMS, exporter);
        createHollowLogRecipe(HollowBlocks.WARPED_HOLLOW_STEM, ItemTags.WARPED_STEMS, exporter);
        createHollowLogRecipe(HollowBlocks.MANGROVE_HOLLOW_LOG, ItemTags.MANGROVE_LOGS, exporter);
        createHollowLogRecipe(HollowBlocks.CHERRY_HOLLOW_LOG, ItemTags.CHERRY_LOGS, exporter);

        HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get().forEach(
                (unWaxed, waxed) -> {
                    if (!(unWaxed instanceof OxidizablePillarBlock)) return;

                    createShapeless(RecipeCategory.BUILDING_BLOCKS, waxed)
                            .input(unWaxed)
                            .input(Items.HONEYCOMB)
                            .group(getItemPath(waxed))
                            .criterion(hasItem(unWaxed), conditionsFromItem(unWaxed))
                            .offerTo(exporter, convertBetween(waxed, Items.HONEYCOMB));
                }
        );

        RegistryWrapper.WrapperLookup lookup = registriesFuture.join();

        createCopperHornRecipe(Instruments.PONDER_GOAT_HORN, CopperInstrument.GREAT_SKY_FALLING, lookup, exporter);
        createCopperHornRecipe(Instruments.SING_GOAT_HORN, CopperInstrument.OLD_HYMN_RESTING, lookup, exporter);
        createCopperHornRecipe(Instruments.SEEK_GOAT_HORN, CopperInstrument.PURE_WATER_DESIRE, lookup, exporter);
        createCopperHornRecipe(Instruments.FEEL_GOAT_HORN, CopperInstrument.HUMBLE_FIRE_MEMORY, lookup, exporter);
        createCopperHornRecipe(Instruments.ADMIRE_GOAT_HORN, CopperInstrument.DRY_URGE_ANGER, lookup, exporter);
        createCopperHornRecipe(Instruments.CALL_GOAT_HORN, CopperInstrument.CLEAR_TEMPER_JOURNEY, lookup, exporter);
        createCopperHornRecipe(Instruments.YEARN_GOAT_HORN, CopperInstrument.FRESH_NEST_THOUGHT, lookup, exporter);
        createCopperHornRecipe(Instruments.DREAM_GOAT_HORN, CopperInstrument.SECRET_LAKE_TEAR, lookup, exporter);

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
                (pillar, slab) -> createShaped(RecipeCategory.BUILDING_BLOCKS, pillar)
                        .input('#', slab)
                        .pattern("##")
                        .criterion(hasItem(slab), conditionsFromItem(slab))
                        .offerTo(exporter)
        );

        createShaped(RecipeCategory.BUILDING_BLOCKS, HollowBlocks.ECHOING_POT)
                .input('P', Items.POLISHED_DEEPSLATE)
                .input('C', Items.CHISELED_DEEPSLATE)
                .input('E', Items.ECHO_SHARD)
                .pattern("P P")
                .pattern("CEC")
                .pattern("CCC")
                .criterion(hasItem(Items.ECHO_SHARD), conditionsFromItem(Items.ECHO_SHARD))
                .offerTo(exporter);

        createShaped(RecipeCategory.BUILDING_BLOCKS, HollowBlocks.JAR, 4)
                .input('P', HollowBlocks.POLYPORE)
                .input('G', ConventionalItemTags.GLASS_BLOCKS)
                .pattern(" P ")
                .pattern("G G")
                .pattern(" G ")
                .criterion(hasItem(HollowBlocks.POLYPORE), conditionsFromItem(HollowBlocks.POLYPORE))
                .offerTo(exporter);

        createShapeless(RecipeCategory.MISC, Items.LIGHT_BLUE_DYE)
                .input(HollowBlocks.ROOTED_ORCHID)
                .criterion(hasItem(HollowBlocks.ROOTED_ORCHID), conditionsFromItem(HollowBlocks.ROOTED_ORCHID))
                .offerTo(exporter, "light_blue_dye_from_rooted_orchid");

        createShapeless(RecipeCategory.MISC, Items.PINK_DYE)
                .input(HollowBlocks.PAEONIA)
                .criterion(hasItem(HollowBlocks.PAEONIA), conditionsFromItem(HollowBlocks.PAEONIA))
                .offerTo(exporter, "pink_dye_from_paeonia");

        createShapeless(RecipeCategory.MISC, Items.WHITE_DYE, 2)
                .input(HollowBlocks.CAMPION)
                .criterion(hasItem(HollowBlocks.CAMPION), conditionsFromItem(HollowBlocks.CAMPION))
                .offerTo(exporter, "white_dye_from_campion");
    }

    private ShapelessRecipeJsonBuilder createShapeless(RecipeCategory recipeCategory, ItemConvertible item) {
        return createShapeless(recipeCategory, item, 1);
    }

    private ShapelessRecipeJsonBuilder createShapeless(RecipeCategory recipeCategory, ItemConvertible item, int count) {
        return new ShapelessRecipeJsonBuilder(recipeCategory, item, count);
    }

    private ShapedRecipeJsonBuilder createShaped(RecipeCategory recipeCategory, ItemConvertible item) {
        return createShaped(recipeCategory, item, 1);
    }

    private ShapedRecipeJsonBuilder createShaped(RecipeCategory recipeCategory, ItemConvertible item, int count) {
        return new ShapedRecipeJsonBuilder(recipeCategory, item, count);
    }

    private void createHollowLogRecipe(Block block, TagKey<Item> LogBlock, RecipeExporter exporter) {
        createShaped(RecipeCategory.DECORATIONS, block, 8)
                .criterion("has_logs", conditionsFromTag(LogBlock))
                .input('#', LogBlock)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .offerTo(exporter);
    }

    public void createCopperHornRecipe(RegistryKey<Instrument> goat, CopperInstrument copper, RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter) {
        RegistryEntryLookup<Instrument> instrumentLookup = wrapperLookup.getWrapperOrThrow(RegistryKeys.INSTRUMENT);

        SpecterShapedRecipeJsonBuilder.create(
                        RecipeCategory.TOOLS, new ItemStack(
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
                                .add(DataComponentTypes.INSTRUMENT, instrumentLookup.getOrThrow(goat))
                                .build()
                ))
                .pattern("#G#")
                .pattern(" # ")
                .criterion("has_goat_horn", conditionsFromItem(Items.GOAT_HORN))
                .offerTo(
                        exporter,
                        Hollow.id("copper_horn" + copper.asString())
                );
    }

    @Override
    public String getName() {
        return "";
    }
}
