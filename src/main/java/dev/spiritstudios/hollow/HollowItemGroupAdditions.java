package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.hollow.world.item.HollowItems;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.function.Consumer;

public final class HollowItemGroupAdditions {
	public static void init() {
		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((itemGroup, entries) -> {
			ItemGroupHelper helper = new ItemGroupHelper(itemGroup, entries);

			Consumer<Block> inserter = block -> {
				if (!(block instanceof HollowLogBlock log)) throw new IllegalStateException();
				helper.insertAfter(BuiltInRegistries.ITEM.getValue(log.typeData.id()), block);
			};

			HollowBlocks.OAK_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.SPRUCE_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.BIRCH_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.JUNGLE_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.ACACIA_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.CHERRY_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.PALE_OAK_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.DARK_OAK_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.MANGROVE_HOLLOW_LOG.forEach(inserter);
			HollowBlocks.CRIMSON_HOLLOW_STEM.forEach(inserter);
			HollowBlocks.WARPED_HOLLOW_STEM.forEach(inserter);

			helper.insertAfter(Items.KELP, HollowItems.CATTAIL);
			helper.insertAfter(Items.LILY_PAD, HollowItems.FLOWERING_LILY_PAD, HollowItems.GIANT_LILY_PAD);
			helper.insertAfter(Items.BROWN_MUSHROOM, HollowItems.POLYPORE);

			helper.insertAfter(Items.DECORATED_POT, HollowItems.JAR, HollowItems.JAR_OF_FIREFLIES);

			helper.insertAfter(Items.SCULK_CATALYST, HollowItems.SCULK_JAW);
			helper.insertAfter(Items.VAULT, HollowItems.ECHOING_POT);
			helper.insertAfter(Items.CHEST, HollowItems.STONE_CHEST, HollowItems.STONE_CHEST_LID);

			WeatheringCopperCollection.zipApply(
					Items.CHISELED_COPPER, HollowItems.COPPER_PILLAR,
					helper::insertAfter
			);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.insertAfter(Items.MUSIC_DISC_WARD, HollowItems.MUSIC_DISC_POSTMORTEM);
			entries.getContext().holders()
					.lookup(HollowRegistryKeys.COPPER_INSTRUMENT)
					.map(wrapper -> wrapper.listElements().map(entry -> {
						ItemStack stack = new ItemStack(HollowItems.COPPER_HORN);
						stack.set(HollowDataComponents.COPPER_INSTRUMENT, new CopperInstrumentComponent(entry));
						return stack;
					}).toArray(ItemStack[]::new))
					.ifPresent(items -> entries.insertAfter(
							Items.GOAT_HORN,
							items
					));
		});
	}

	private record ItemGroupHelper(CreativeModeTab group, FabricCreativeModeTabOutput entries) {
		public void insertAfter(Item after, ItemLike... add) {
			if (group.contains(after.getDefaultInstance())) {
                entries.insertAfter(after, add);
            }
		}
	}
}
