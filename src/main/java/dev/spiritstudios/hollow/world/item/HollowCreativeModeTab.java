package dev.spiritstudios.hollow.world.item;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import dev.spiritstudios.hollow.world.level.block.LogCollection;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.List;

public final class HollowCreativeModeTab {
	private static void insertHollowLogs(LogCollection<Block> collection, FabricCreativeModeTabOutput output) {
		collection.forEach(block -> {
			if (!(block instanceof HollowLogBlock hollowLog)) throw new IllegalStateException();
			output.insertAfter(hollowLog.log.asItem(), block);
		});
	}

	private static List<ItemStack> asStacks(WeatheringCopperCollection.ByState<Item> byState) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builderWithExpectedSize(4);
		byState.forEach(item -> builder.add(item.getDefaultInstance()));
		return builder.build();
	}

	private static List<ItemStack> asStacks(LogCollection<Item> byState) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builderWithExpectedSize(4);
		byState.forEach(item -> builder.add(item.getDefaultInstance()));
		return builder.build();
	}

	private static void registerHollowTab() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Hollow.id("hollow_tab"), FabricCreativeModeTab.builder()
			.icon(HollowItems.HOLLOW_LOG.birch()::getDefaultInstance)
			.title(Component.translatable("item_group.hollow.hollow"))
			.displayItems((parameters, output) -> {
				output.acceptAll(asStacks(HollowItems.HOLLOW_LOG));
				output.acceptAll(asStacks(HollowItems.STRIPPED_HOLLOW_LOG));

				output.acceptAll(asStacks(HollowItems.COPPER_PILLAR.weathering()));
				output.acceptAll(asStacks(HollowItems.COPPER_PILLAR.waxed()));

				output.accept(HollowItems.FLOWERING_LILY_PAD);
				output.accept(HollowItems.CATTAIL);
				output.accept(HollowItems.POLYPORE);
				output.accept(HollowItems.SWITCHGRASS);
				output.accept(HollowItems.GLASS_JAR);
				output.accept(HollowItems.FIREFLY_JAR);

				output.accept(HollowItems.STONE_CHEST);
				output.accept(HollowItems.STONE_CHEST_LID);
				output.accept(HollowItems.ECHOING_POT);
				output.accept(HollowItems.SCULK_JAW);

				output.accept(HollowItems.OAK_FURNACE_BOAT);
				output.accept(HollowItems.SPRUCE_FURNACE_BOAT);
				output.accept(HollowItems.BIRCH_FURNACE_BOAT);
				output.accept(HollowItems.JUNGLE_FURNACE_BOAT);
				output.accept(HollowItems.ACACIA_FURNACE_BOAT);
				output.accept(HollowItems.DARK_OAK_FURNACE_BOAT);
				output.accept(HollowItems.MANGROVE_FURNACE_BOAT);
				output.accept(HollowItems.CHERRY_FURNACE_BOAT);
				output.accept(HollowItems.PALE_OAK_FURNACE_BOAT);
				output.accept(HollowItems.BAMBOO_FURNACE_RAFT);

				parameters.holders()
					.lookup(HollowRegistries.COPPER_INSTRUMENT)
					.map(wrapper -> wrapper.listElements().map(entry -> {
						ItemStack stack = new ItemStack(HollowItems.COPPER_HORN);
						stack.set(HollowDataComponents.COPPER_INSTRUMENT, new CopperInstrumentComponent(entry));
						return stack;
					}).toList())
					.ifPresent(output::acceptAll);

				output.accept(HollowItems.MUSIC_DISC_POSTMORTEM);
				output.accept(HollowItems.MUSIC_DISC_ONLY_YOU);
			})
			.build()
		);
	}

	public static void init() {
		registerHollowTab();

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
			output.insertAfter(Items.CHEST, HollowItems.STONE_CHEST, HollowItems.STONE_CHEST_LID);
			output.insertAfter(Items.DECORATED_POT, HollowItems.ECHOING_POT, HollowItems.GLASS_JAR, HollowItems.FIREFLY_JAR);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output -> {
			insertHollowLogs(HollowBlocks.HOLLOW_LOG, output);
			insertHollowLogs(HollowBlocks.STRIPPED_HOLLOW_LOG, output);

			output.insertAfter(Items.CHISELED_COPPER.weathering().oxidized(), asStacks(HollowItems.COPPER_PILLAR.weathering()));
			output.insertAfter(Items.CHISELED_COPPER.waxed().oxidized(), asStacks(HollowItems.COPPER_PILLAR.waxed()));
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(output -> {
			insertHollowLogs(HollowBlocks.HOLLOW_LOG, output);

			output.insertAfter(Items.LILY_PAD, HollowItems.FLOWERING_LILY_PAD, HollowItems.CATTAIL);
			output.insertAfter(Items.BROWN_MUSHROOM, HollowItems.POLYPORE);
			output.insertBefore(Items.FIREFLY_BUSH, HollowItems.SWITCHGRASS);

			output.insertAfter(Items.SCULK_CATALYST, HollowItems.SCULK_JAW);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.insertAfter(Items.MUSIC_DISC_WARD, HollowItems.MUSIC_DISC_POSTMORTEM, HollowItems.MUSIC_DISC_ONLY_YOU);

			entries.insertAfter(Items.OAK_CHEST_BOAT, HollowItems.OAK_FURNACE_BOAT);
			entries.insertAfter(Items.SPRUCE_CHEST_BOAT, HollowItems.SPRUCE_FURNACE_BOAT);
			entries.insertAfter(Items.BIRCH_CHEST_BOAT, HollowItems.BIRCH_FURNACE_BOAT);
			entries.insertAfter(Items.JUNGLE_CHEST_BOAT, HollowItems.JUNGLE_FURNACE_BOAT);
			entries.insertAfter(Items.ACACIA_CHEST_BOAT, HollowItems.ACACIA_FURNACE_BOAT);
			entries.insertAfter(Items.DARK_OAK_CHEST_BOAT, HollowItems.DARK_OAK_FURNACE_BOAT);
			entries.insertAfter(Items.MANGROVE_CHEST_BOAT, HollowItems.MANGROVE_FURNACE_BOAT);
			entries.insertAfter(Items.CHERRY_CHEST_BOAT, HollowItems.CHERRY_FURNACE_BOAT);
			entries.insertAfter(Items.PALE_OAK_CHEST_BOAT, HollowItems.PALE_OAK_FURNACE_BOAT);
			entries.insertAfter(Items.BAMBOO_CHEST_RAFT, HollowItems.BAMBOO_FURNACE_RAFT);

			entries.getContext().holders()
				.lookup(HollowRegistries.COPPER_INSTRUMENT)
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

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
			entries.insertAfter(Items.OAK_CHEST_BOAT, HollowItems.OAK_FURNACE_BOAT);
			entries.insertAfter(Items.BAMBOO_CHEST_RAFT, HollowItems.BAMBOO_FURNACE_RAFT);

			entries.insertAfter(Items.COPPER_CHEST.waxed().unaffected(), HollowItems.STONE_CHEST, HollowItems.STONE_CHEST_LID);
			entries.insertAfter(Items.DECORATED_POT, HollowItems.ECHOING_POT, HollowItems.GLASS_JAR, HollowItems.FIREFLY_JAR);
		});
	}
}
