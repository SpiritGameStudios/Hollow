package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.mixin.accessor.ShapedRecipeBuilderAccessor;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.item.CopperInstrument;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.world.item.CopperInstruments;
import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.world.item.HollowItems;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import dev.spiritstudios.hollow.world.level.block.HollowLogCollection;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.concurrent.CompletableFuture;

public class HollowRecipeProvider extends FabricRecipeProvider {
    public HollowRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput output) {
        return new RecipeProvider(wrapperLookup, output) {
            @Override
            public void buildRecipes() {
                createHollowLogRecipe(
                        HollowBlocks.OAK_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.SPRUCE_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.BIRCH_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.JUNGLE_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.ACACIA_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.DARK_OAK_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.CRIMSON_HOLLOW_STEM,
                        new HollowLogCollection<>(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM)
                );
                createHollowLogRecipe(
                        HollowBlocks.WARPED_HOLLOW_STEM,
                        new HollowLogCollection<>(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM)
                );
                createHollowLogRecipe(
                        HollowBlocks.MANGROVE_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.CHERRY_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG)
                );
                createHollowLogRecipe(
                        HollowBlocks.PALE_OAK_HOLLOW_LOG,
                        new HollowLogCollection<>(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG)
                );

                waxRecipes(FeatureFlagSet.of());

                createCopperHornRecipe(Instruments.PONDER_GOAT_HORN, CopperInstruments.GREAT_SKY_FALLING);
                createCopperHornRecipe(Instruments.SING_GOAT_HORN, CopperInstruments.OLD_HYMN_RESTING);
                createCopperHornRecipe(Instruments.SEEK_GOAT_HORN, CopperInstruments.PURE_WATER_DESIRE);
                createCopperHornRecipe(Instruments.FEEL_GOAT_HORN, CopperInstruments.HUMBLE_FIRE_MEMORY);
                createCopperHornRecipe(Instruments.ADMIRE_GOAT_HORN, CopperInstruments.DRY_URGE_ANGER);
                createCopperHornRecipe(Instruments.CALL_GOAT_HORN, CopperInstruments.CLEAR_TEMPER_JOURNEY);
                createCopperHornRecipe(Instruments.YEARN_GOAT_HORN, CopperInstruments.FRESH_NEST_THOUGHT);
                createCopperHornRecipe(Instruments.DREAM_GOAT_HORN, CopperInstruments.SECRET_LAKE_TEAR);

                WeatheringCopperCollection.zipApply(
                        HollowBlocks.COPPER_PILLAR,
                        Blocks.CUT_COPPER_SLAB,
                        (pillar, slab) -> shaped(RecipeCategory.BUILDING_BLOCKS, pillar)
                                .define('#', slab)
                                .pattern("##")
                                .unlockedBy(getHasName(slab), has(slab))
                                .save(this.output)
                );

                shaped(RecipeCategory.BUILDING_BLOCKS, HollowBlocks.ECHOING_POT)
                        .define('P', Items.POLISHED_DEEPSLATE)
                        .define('C', Items.CHISELED_DEEPSLATE)
                        .define('E', Items.ECHO_SHARD)
                        .pattern("P P")
                        .pattern("CEC")
                        .pattern("CCC")
                        .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                        .save(this.output);

                shaped(RecipeCategory.BUILDING_BLOCKS, HollowBlocks.JAR, 4)
                        .define('P', HollowBlocks.POLYPORE)
                        .define('G', ConventionalItemTags.GLASS_BLOCKS)
                        .pattern(" P ")
                        .pattern("G G")
                        .pattern(" G ")
                        .unlockedBy(getHasName(HollowBlocks.POLYPORE), has(HollowBlocks.POLYPORE))
                        .save(this.output);
            }

            private void createHollowLogRecipe(HollowLogCollection<Block> blocks, HollowLogCollection<Block> ingredients) {
                HollowLogCollection.zipApply(blocks, ingredients, (block, ingredient) -> {
                    shaped(RecipeCategory.DECORATIONS, block, 8)
                            .unlockedBy("has_logs", has(ingredient))
                            .define('#', ingredient)
                            .pattern("###")
                            .pattern("# #")
                            .pattern("###")
                            .save(this.output);
                });
            }

            public void createCopperHornRecipe(ResourceKey<Instrument> goat, ResourceKey<CopperInstrument> copper) {
                HolderGetter<Instrument> instruments = wrapperLookup.lookupOrThrow(Registries.INSTRUMENT);
                HolderGetter<CopperInstrument> copperInstruments = wrapperLookup.lookupOrThrow(HollowRegistries.COPPER_INSTRUMENT);


                ShapedRecipeBuilderAccessor.create(
                                registries.lookupOrThrow(Registries.ITEM),
                                RecipeCategory.TOOLS,
                                new ItemStackTemplate(
                                        HollowItems.COPPER_HORN,
                                        DataComponentPatch.builder()
                                                .set(
                                                        HollowDataComponents.COPPER_INSTRUMENT,
                                                        new CopperInstrumentComponent(copperInstruments.getOrThrow(copper))
                                                )
                                                .build()
                                )
                        )
                        .group("hollow_copper_horn")
                        .define('#', Ingredient.of(Items.COPPER_INGOT))
                        .define('G', DefaultCustomIngredients.components(
                                Ingredient.of(Items.GOAT_HORN),
                                changes -> changes.set(
                                        DataComponents.INSTRUMENT,
                                        new InstrumentComponent(instruments.getOrThrow(goat))
                                )

                        ))
                        .pattern("#G#")
                        .pattern(" # ")
                        .unlockedBy("has_goat_horn", has(Items.GOAT_HORN))
                        .save(
                                this.output,
                                ResourceKey.create(
                                        Registries.RECIPE,
                                        Hollow.id("copper_horn_" + copper.identifier().toDebugFileName())
                                )
                        );
            }

        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
