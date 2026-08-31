package dev.spiritstudios.hollow.world.entity;

import dev.spiritstudios.hollow.references.HollowEntityTypeIds;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import dev.spiritstudios.hollow.world.entity.vehicle.FurnaceBoat;
import dev.spiritstudios.hollow.world.entity.vehicle.FurnaceRaft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public final class HollowEntityTypes {
	public static final EntityType<FurnaceBoat> ACACIA_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.ACACIA_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceRaft> BAMBOO_FURNACE_RAFT = registerFurnaceBoat(
		HollowEntityTypeIds.BAMBOO_FURNACE_RAFT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> BIRCH_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.BIRCH_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> CHERRY_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.CHERRY_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> DARK_OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.DARK_OAK_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> JUNGLE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.JUNGLE_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> MANGROVE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.MANGROVE_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.OAK_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> PALE_OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.PALE_OAK_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);
	public static final EntityType<FurnaceBoat> SPRUCE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.SPRUCE_FURNACE_BOAT,
		Items.ACACIA_CHEST_BOAT
	);

	@SuppressWarnings("unchecked")
	private static <T extends AbstractFurnaceBoat> EntityType<T> registerFurnaceBoat(ResourceKey<EntityType<?>> id, Item dropItem) {
		EntityType.EntityFactory<? extends AbstractFurnaceBoat> factory = (entityType, level) -> {
			Supplier<Item> itemSupplier = () -> dropItem;

			if (id.equals(HollowEntityTypeIds.BAMBOO_FURNACE_RAFT))
				return new FurnaceRaft(entityType, level, itemSupplier);

			return new FurnaceBoat(entityType, level, itemSupplier);
		};
		return (EntityType<T>) register(id, EntityType.Builder.of(factory, MobCategory.MISC)
			.noLootTable()
			.sized(1.375F, 0.5625F)
			.eyeHeight(0.5625F)
			.clientTrackingRange(10)
		);
	}

	private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> id, EntityType.Builder<T> builder) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id));
	}

	public static void init() {
		// NO-OP
	}
}
