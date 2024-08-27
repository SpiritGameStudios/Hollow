package dev.spiritstudios.hollow.datagen;

import com.mojang.datafixers.util.Either;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.hollow.registry.HollowItemRegistrar;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import dev.spiritstudios.specter.api.item.datagen.SpecterItemGroupProvider;
import dev.spiritstudios.specter.api.item.DataItemGroup;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class ItemGroupProvider extends SpecterItemGroupProvider {
    public ItemGroupProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    protected void configure(BiConsumer<Identifier, DataItemGroup> provider, RegistryWrapper.WrapperLookup lookup) {
        List<ItemStack> items = new ArrayList<>();
        ReflectionHelper.forEachStaticField(
                HollowBlockRegistrar.class,
                Block.class,
                (block, name, field) -> {
                    ItemStack stack = new ItemStack(block.asItem());
                    if (!stack.isEmpty()) items.add(stack);
                }
        );
        items.add(new ItemStack(HollowItemRegistrar.FIREFLY_SPAWN_EGG));
        items.add(new ItemStack(HollowItemRegistrar.MUSIC_DISC_POSTMORTEM));

        provider.accept(
                Identifier.of(MODID, "hollow"),
                new DataItemGroup(
                        "item_group.hollow",
                        new ItemStack(HollowBlockRegistrar.BIRCH_HOLLOW_LOG),
                        items.stream().map(Either::<Item, ItemStack>right).toList()
                )
        );
    }
}
