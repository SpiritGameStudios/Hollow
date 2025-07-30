package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.block.HollowBlocks;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.component.HollowDataComponentTypes;
import dev.spiritstudios.hollow.item.HollowItems;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

import java.util.Map;

public final class HollowItemGroupAdditions {
	public static void init() {
		Map<Item, HollowLogBlock> baseToHollow = new Object2ReferenceOpenHashMap<>();
		ReflectionHelper.getStaticFields(HollowBlocks.class, HollowLogBlock.class)
				.forEach(pair ->
						baseToHollow.put(Registries.ITEM.get(pair.value().typeData.id()), pair.value()));

		ItemGroupEvents.MODIFY_ENTRIES_ALL.register((itemGroup, entries) -> {
			ItemGroupHelper helper = new ItemGroupHelper(itemGroup, entries);

			baseToHollow.forEach(helper::addAfter);

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
			entries.addAfter(Items.MUSIC_DISC_WARD, HollowItems.MUSIC_DISC_POSTMORTEM);
			entries.getContext().lookup()
					.getOptional(HollowRegistryKeys.COPPER_INSTRUMENT)
					.map(wrapper -> wrapper.streamEntries().map(entry -> {
						ItemStack stack = new ItemStack(HollowItems.COPPER_HORN);
						stack.set(HollowDataComponentTypes.COPPER_INSTRUMENT, new CopperInstrumentComponent(entry));
						return stack;
					}).toArray(ItemStack[]::new))
					.ifPresent(items -> entries.addAfter(
							Items.GOAT_HORN,
							items
					));
		});
	}

	private record ItemGroupHelper(ItemGroup group, FabricItemGroupEntries entries) {
		public void addAfter(Item after, ItemConvertible... add) {
			if (group.contains(after.getDefaultStack()))
				entries.addAfter(after, add);
		}
	}
}
