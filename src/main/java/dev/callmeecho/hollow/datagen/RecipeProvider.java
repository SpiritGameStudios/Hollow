package dev.callmeecho.hollow.datagen;

import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        createHollowLogRecipe(exporter, HollowBlockRegistry.OAK_HOLLOW_LOG, ItemTags.OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.SPRUCE_HOLLOW_LOG, ItemTags.SPRUCE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.BIRCH_HOLLOW_LOG, ItemTags.BIRCH_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.JUNGLE_HOLLOW_LOG, ItemTags.JUNGLE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.ACACIA_HOLLOW_LOG, ItemTags.ACACIA_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.DARK_OAK_HOLLOW_LOG, ItemTags.DARK_OAK_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.CRIMSON_HOLLOW_STEM, ItemTags.CRIMSON_STEMS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.WARPED_HOLLOW_STEM, ItemTags.WARPED_STEMS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.MANGROVE_HOLLOW_LOG, ItemTags.MANGROVE_LOGS);
        createHollowLogRecipe(exporter, HollowBlockRegistry.CHERRY_HOLLOW_LOG, ItemTags.CHERRY_LOGS);
    }
    
    public void createHollowLogRecipe(Consumer<RecipeJsonProvider> exporter, HollowLogBlock block, TagKey<Item> LogBlock) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, block, 8)
                .criterion("has_logs", FabricRecipeProvider.conditionsFromTag(LogBlock))
                .input('#', LogBlock)
                .pattern("###")
                .pattern("# #")
                .pattern("###")
                .offerTo(exporter);
    }
}
