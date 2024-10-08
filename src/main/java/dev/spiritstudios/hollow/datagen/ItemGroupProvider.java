package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.hollow.registry.HollowDataComponentRegistrar;
import dev.spiritstudios.hollow.registry.HollowItemRegistrar;
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
                HollowBlockRegistrar.class,
                Block.class,
                (block, name, field) -> {
                    ItemStack stack = new ItemStack(block.asItem());
                    if (!stack.isEmpty()) items.add(stack);
                }
        );

        Arrays.stream(HollowDataComponentRegistrar.CopperInstrument.values()).map(instrument -> new ItemStack(
                Registries.ITEM.getEntry(HollowItemRegistrar.COPPER_HORN),
                1,
                ComponentChanges.builder()
                        .add(HollowDataComponentRegistrar.COPPER_INSTRUMENT, instrument)
                        .build()
        )).forEach(items::add);

        items.add(HollowItemRegistrar.FIREFLY_SPAWN_EGG.getDefaultStack());
        items.add(HollowItemRegistrar.MUSIC_DISC_POSTMORTEM.getDefaultStack());

        provider.accept(
                Identifier.of(MODID, "hollow"),
                ItemGroupData.of(
                        Identifier.of(MODID, "hollow"),
                        HollowBlockRegistrar.BIRCH_HOLLOW_LOG,
                        items
                )
        );
    }
}
