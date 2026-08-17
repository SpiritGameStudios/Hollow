package dev.spiritstudios.hollow.mixin.accessor;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShapedRecipeBuilder.class)
public interface ShapedRecipeBuilderAccessor {
    @Invoker("<init>")
    static ShapedRecipeBuilder create(HolderGetter<Item> items, RecipeCategory category, ItemStackTemplate result) {
        throw new IllegalStateException("Implemented via mixin.");
    }
}