package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.component.CopperInstrument;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;

import java.util.Arrays;
import java.util.Map;

public final class HollowItemGroupAdditions {
    public static void init() {
        Map<Item, HollowLogBlock> baseToHollow = new Object2ReferenceOpenHashMap<>();
        ReflectionHelper.forEachStaticField(HollowBlocks.class, HollowLogBlock.class, (block, name, field) -> baseToHollow.put(Registries.ITEM.get(block.typeData.id()), block));

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, entries) -> {
            ItemGroupHelper helper = new ItemGroupHelper(itemGroup, entries);

            baseToHollow.forEach(helper::addAfter);

            helper.addAfter(
                    Items.PINK_PETALS,
                    HollowBlocks.PINK_WILDFLOWER,
                    HollowBlocks.PURPLE_WILDFLOWER,
                    HollowBlocks.BLUE_WILDFLOWER,
                    HollowBlocks.WHITE_WILDFLOWER
            );
            helper.addAfter(Items.ALLIUM, HollowBlocks.PAEONIA);
            helper.addAfter(Items.BLUE_ORCHID, HollowBlocks.ROOTED_ORCHID);
            helper.addAfter(Items.PEONY, HollowBlocks.CAMPION);
            helper.addAfter(Items.DEAD_BUSH, HollowBlocks.TWIG);
            helper.addAfter(Items.KELP, HollowBlocks.CATTAIL);
            helper.addAfter(Items.LILY_PAD, HollowItems.LOTUS_LILYPAD, HollowItems.GIANT_LILYPAD);
            helper.addAfter(Items.BROWN_MUSHROOM, HollowBlocks.POLYPORE);

            helper.addAfter(Items.DECORATED_POT, HollowBlocks.JAR, HollowBlocks.FIREFLY_JAR);

            helper.addAfter(Items.SCULK_CATALYST, HollowBlocks.SCULK_JAW);
            helper.addAfter(Items.VAULT, HollowBlocks.ECHOING_POT);
            helper.addAfter(Items.CHEST, HollowBlocks.STONE_CHEST, HollowBlocks.STONE_CHEST_LID);

            helper.addAfter(Items.CHISELED_COPPER, HollowBlocks.COPPER_PILLAR);
            helper.addAfter(Items.EXPOSED_CHISELED_COPPER, HollowBlocks.EXPOSED_COPPER_PILLAR);
            helper.addAfter(Items.WEATHERED_CHISELED_COPPER, HollowBlocks.WEATHERED_COPPER_PILLAR);
            helper.addAfter(Items.OXIDIZED_CHISELED_COPPER, HollowBlocks.OXIDIZED_COPPER_PILLAR);

            helper.addAfter(Items.WAXED_CHISELED_COPPER, HollowBlocks.WAXED_COPPER_PILLAR);
            helper.addAfter(Items.WAXED_EXPOSED_CHISELED_COPPER, HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR);
            helper.addAfter(Items.WAXED_WEATHERED_CHISELED_COPPER, HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR);
            helper.addAfter(Items.WAXED_OXIDIZED_CHISELED_COPPER, HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(HollowItems.FIREFLY_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(HollowItems.MUSIC_DISC_POSTMORTEM);


            entries.addAfter(
                    Items.GOAT_HORN,
                    Arrays.stream(CopperInstrument.values()).map(instrument -> new ItemStack(
                            Registries.ITEM.getEntry(HollowItems.COPPER_HORN),
                            1,
                            ComponentChanges.builder()
                                    .add(HollowDataComponentTypes.COPPER_INSTRUMENT, instrument)
                                    .build()
                    )).toArray(ItemStack[]::new)
            );
        });
    }

    private record ItemGroupHelper(ItemGroup group, FabricItemGroupEntries entries) {
        public void addAfter(Item after, ItemConvertible... add) {
            if (group.contains(after.getDefaultStack()))
                entries.addAfter(after, add);
        }
    }
}
