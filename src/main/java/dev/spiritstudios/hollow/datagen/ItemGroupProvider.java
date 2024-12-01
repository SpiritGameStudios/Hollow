package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import dev.spiritstudios.specter.api.item.datagen.SpecterItemGroupProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class ItemGroupProvider extends SpecterItemGroupProvider {
    public ItemGroupProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    protected void generate(BiConsumer<Identifier, ItemGroupData> provider, RegistryWrapper.WrapperLookup lookup) {
        List<ItemStack> items = new ArrayList<>();
        ReflectionHelper.forEachStaticField(
                HollowBlocks.class,
                Block.class,
                (block, name, field) -> {
                    ItemStack stack = new ItemStack(block.asItem());
                    if (!stack.isEmpty()) items.add(stack);
                }
        );

        Arrays.stream(CopperInstrument.values()).map(instrument -> new ItemStack(
                Registries.ITEM.getEntry(HollowItems.COPPER_HORN),
                1,
                ComponentChanges.builder()
                        .add(HollowDataComponentTypes.COPPER_INSTRUMENT, instrument)
                        .build()
        )).forEach(items::add);

        items.add(HollowItems.FIREFLY_SPAWN_EGG.getDefaultStack());
        items.add(HollowItems.MUSIC_DISC_POSTMORTEM.getDefaultStack());

        provider.accept(
                Identifier.of(MODID, "hollow"),
                ItemGroupData.of(
                        Identifier.of(MODID, "hollow"),
                        HollowBlocks.BIRCH_HOLLOW_LOG,
                        items
                )
        );
    }
}
