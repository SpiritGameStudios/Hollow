package dev.spiritstudios.hollow.world.item;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.List;
import java.util.function.Consumer;

public final class HollowCreativeModeTab {
	private static void insertHollowLogs(FabricCreativeModeTabOutput output) {
		Consumer<Block> inserter = block -> {
			if (!(block instanceof HollowLogBlock log)) throw new IllegalStateException();
			output.insertAfter(log.log.asItem(), block);
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
	}

	private static List<ItemStack> asStacks(WeatheringCopperCollection.ByState<Item> byState) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builderWithExpectedSize(4);
		byState.forEach(item -> builder.add(item.getDefaultInstance()));
		return builder.build();
	}

	public static void init() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
			output.insertAfter(Items.CHEST, HollowItems.STONE_CHEST, HollowItems.STONE_CHEST_LID);
			output.insertAfter(Items.DECORATED_POT, HollowItems.ECHOING_POT, HollowItems.JAR, HollowItems.JAR_OF_FIREFLIES);
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output -> {
			insertHollowLogs(output);

			output.insertAfter(Items.CHISELED_COPPER.weathering().oxidized(), asStacks(HollowItems.COPPER_PILLAR.weathering()));
			output.insertAfter(Items.CHISELED_COPPER.waxed().oxidized(), asStacks(HollowItems.COPPER_PILLAR.waxed()));
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(output -> {
			output.insertAfter(Items.LILY_PAD, HollowItems.FLOWERING_LILY_PAD, HollowItems.GIANT_LILY_PAD, HollowItems.CATTAIL);
			output.insertAfter(Items.BROWN_MUSHROOM, HollowItems.POLYPORE);
			output.insertBefore(Items.FIREFLY_BUSH, HollowItems.SWITCHGRASS);

			output.insertAfter(Items.SCULK_CATALYST, HollowItems.SCULK_JAW);

			Consumer<Block> inserter = block -> {
				if (!(block instanceof HollowLogBlock log)) throw new IllegalStateException();
				output.insertAfter(log.log.asItem(), block);
			};

			inserter.accept(HollowBlocks.OAK_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.SPRUCE_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.BIRCH_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.JUNGLE_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.ACACIA_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.CHERRY_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.PALE_OAK_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.DARK_OAK_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.MANGROVE_HOLLOW_LOG.hollowLog());
			inserter.accept(HollowBlocks.CRIMSON_HOLLOW_STEM.hollowLog());
			inserter.accept(HollowBlocks.WARPED_HOLLOW_STEM.hollowLog());
		});

		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.insertAfter(Items.MUSIC_DISC_WARD, HollowItems.MUSIC_DISC_POSTMORTEM);

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
	}
}
