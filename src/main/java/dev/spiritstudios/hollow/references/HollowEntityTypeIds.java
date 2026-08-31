package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public final class HollowEntityTypeIds {
	public static final ResourceKey<EntityType<?>> ACACIA_FURNACE_BOAT = create("acacia_furnace_boat");
	public static final ResourceKey<EntityType<?>> BAMBOO_FURNACE_RAFT = create("bamboo_furnace_raft");
	public static final ResourceKey<EntityType<?>> BIRCH_FURNACE_BOAT = create("birch_furnace_boat");
	public static final ResourceKey<EntityType<?>> CHERRY_FURNACE_BOAT = create("cherry_furnace_boat");
	public static final ResourceKey<EntityType<?>> DARK_OAK_FURNACE_BOAT = create("dark_oak_furnace_boat");
	public static final ResourceKey<EntityType<?>> JUNGLE_FURNACE_BOAT = create("jungle_furnace_boat");
	public static final ResourceKey<EntityType<?>> MANGROVE_FURNACE_BOAT = create("mangrove_furnace_boat");
	public static final ResourceKey<EntityType<?>> OAK_FURNACE_BOAT = create("oak_furnace_boat");
	public static final ResourceKey<EntityType<?>> PALE_OAK_FURNACE_BOAT = create("pale_oak_furnace_boat");
	public static final ResourceKey<EntityType<?>> SPRUCE_FURNACE_BOAT = create("spruce_furnace_boat");

	private static ResourceKey<EntityType<?>> create(String name) {
		return ResourceKey.create(Registries.ENTITY_TYPE, Hollow.id(name));
	}
}
