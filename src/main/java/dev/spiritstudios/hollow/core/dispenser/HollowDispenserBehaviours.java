package dev.spiritstudios.hollow.core.dispenser;

import dev.spiritstudios.hollow.world.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.world.item.HollowItems;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;

public final class HollowDispenserBehaviours {
	private HollowDispenserBehaviours() {}

	private static void registerBoat(Item boatItem, EntityType<? extends AbstractBoat> type) {
		DispenserBlock.registerBehavior(boatItem, new BoatDispenseItemBehavior(type));
	}

	public static void init() {
		registerBoat(HollowItems.ACACIA_FURNACE_BOAT, HollowEntityTypes.ACACIA_FURNACE_BOAT);
		registerBoat(HollowItems.BAMBOO_FURNACE_RAFT, HollowEntityTypes.BAMBOO_FURNACE_RAFT);
		registerBoat(HollowItems.BIRCH_FURNACE_BOAT, HollowEntityTypes.BIRCH_FURNACE_BOAT);
		registerBoat(HollowItems.CHERRY_FURNACE_BOAT, HollowEntityTypes.CHERRY_FURNACE_BOAT);
		registerBoat(HollowItems.DARK_OAK_FURNACE_BOAT, HollowEntityTypes.DARK_OAK_FURNACE_BOAT);
		registerBoat(HollowItems.JUNGLE_FURNACE_BOAT, HollowEntityTypes.JUNGLE_FURNACE_BOAT);
		registerBoat(HollowItems.MANGROVE_FURNACE_BOAT, HollowEntityTypes.MANGROVE_FURNACE_BOAT);
		registerBoat(HollowItems.OAK_FURNACE_BOAT, HollowEntityTypes.OAK_FURNACE_BOAT);
		registerBoat(HollowItems.PALE_OAK_FURNACE_BOAT, HollowEntityTypes.PALE_OAK_FURNACE_BOAT);
		registerBoat(HollowItems.SPRUCE_FURNACE_BOAT, HollowEntityTypes.SPRUCE_FURNACE_BOAT);
	}
}
