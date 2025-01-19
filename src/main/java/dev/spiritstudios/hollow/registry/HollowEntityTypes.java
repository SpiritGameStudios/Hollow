package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

@SuppressWarnings("unused")
public final class HollowEntityTypes {
    public static final EntityType<FireflyEntity> FIREFLY = register(
            "firefly",
            EntityType.Builder
                    .create(FireflyEntity::new, SpawnGroup.AMBIENT)
                    .dimensions(0.125F, 0.0625F)
                    .maxTrackingRange(20)
    );

    public static final class Tags {
        public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("immune_to_sculk_jaw"));

        public static final TagKey<EntityType<?>> POISONS_FROG = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("poisons_frog"));
    }

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id(id));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static void init() {
        // NO-OP
    }
}
