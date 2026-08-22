package dev.spiritstudios.hollow.world.entity;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@SuppressWarnings("unused")
public final class HollowEntityTypes {
    public static final class Tags {
        public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW = TagKey.create(Registries.ENTITY_TYPE, Hollow.id("immune_to_sculk_jaw"));

        public static final TagKey<EntityType<?>> POISONS_FROG = TagKey.create(Registries.ENTITY_TYPE, Hollow.id("poisons_frog"));

		public static final TagKey<EntityType<?>> CAN_CLIMB_HOLLOW_LOGS = TagKey.create(Registries.ENTITY_TYPE, Hollow.id("can_climb_hollow_logs"));
    }

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key));
    }

    private static ResourceKey<EntityType<?>> keyOf(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Hollow.id(id));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static void init() {
        // NO-OP
    }
}
