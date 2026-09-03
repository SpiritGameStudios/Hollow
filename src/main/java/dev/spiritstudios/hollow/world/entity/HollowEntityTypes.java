package dev.spiritstudios.hollow.world.entity;

import dev.spiritstudios.hollow.references.HollowEntityTypeIds;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import dev.spiritstudios.hollow.world.entity.vehicle.FurnaceBoat;
import dev.spiritstudios.hollow.world.entity.vehicle.FurnaceRaft;
import dev.spiritstudios.hollow.world.item.HollowItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class HollowEntityTypes {
	public static final EntityType<FurnaceBoat> ACACIA_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.ACACIA_FURNACE_BOAT,
		() -> HollowItems.ACACIA_FURNACE_BOAT
	);
	public static final EntityType<FurnaceRaft> BAMBOO_FURNACE_RAFT = registerFurnaceBoat(
		HollowEntityTypeIds.BAMBOO_FURNACE_RAFT,
		() -> HollowItems.BAMBOO_FURNACE_RAFT
	);
	public static final EntityType<FurnaceBoat> BIRCH_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.BIRCH_FURNACE_BOAT,
		() -> HollowItems.BIRCH_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> CHERRY_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.CHERRY_FURNACE_BOAT,
		() -> HollowItems.CHERRY_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> DARK_OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.DARK_OAK_FURNACE_BOAT,
		() -> HollowItems.DARK_OAK_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> JUNGLE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.JUNGLE_FURNACE_BOAT,
		() -> HollowItems.JUNGLE_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> MANGROVE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.MANGROVE_FURNACE_BOAT,
		() -> HollowItems.MANGROVE_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.OAK_FURNACE_BOAT,
		() -> HollowItems.OAK_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> PALE_OAK_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.PALE_OAK_FURNACE_BOAT,
		() -> HollowItems.PALE_OAK_FURNACE_BOAT
	);
	public static final EntityType<FurnaceBoat> SPRUCE_FURNACE_BOAT = registerFurnaceBoat(
		HollowEntityTypeIds.SPRUCE_FURNACE_BOAT,
		() -> HollowItems.SPRUCE_FURNACE_BOAT
	);

	@SuppressWarnings("unchecked")
	private static <T extends AbstractFurnaceBoat> EntityType<T> registerFurnaceBoat(ResourceKey<EntityType<?>> id, Supplier<Item> dropItem) {
		EntityType.EntityFactory<? extends AbstractFurnaceBoat> factory = (entityType, level) -> {
			if (id.equals(HollowEntityTypeIds.BAMBOO_FURNACE_RAFT))
				return new FurnaceRaft(entityType, level, dropItem);

			return new FurnaceBoat(entityType, level, dropItem);
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
