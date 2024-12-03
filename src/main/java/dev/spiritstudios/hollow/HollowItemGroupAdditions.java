package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Map;

public final class HollowItemGroupAdditions {
    public static void init() {
        Map<Item, HollowLogBlock> baseToHollow = new Object2ReferenceOpenHashMap<>();
        ReflectionHelper.forEachStaticField(HollowBlocks.class, HollowLogBlock.class, (block, name, field) -> baseToHollow.put(Registries.ITEM.get(block.typeData.id()), block));

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, entries) -> {
            baseToHollow.forEach((item, block) -> {
                if (itemGroup.contains(item.getDefaultStack())) entries.addAfter(item, block);
            });

            if (itemGroup.contains(Items.PINK_PETALS.getDefaultStack())) {
                entries.addAfter(
                        Items.PINK_PETALS.getDefaultStack(),
                        HollowBlocks.PURPLE_WILDFLOWER,
                        HollowBlocks.BLUE_WILDFLOWER,
                        HollowBlocks.WHITE_WILDFLOWER
                );
            }
        });
    }
}
