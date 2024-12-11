package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

@SuppressWarnings("unused")
public final class HollowEntityTypes {
    public static final EntityType<FireflyEntity> FIREFLY = EntityType.Builder
            .create(FireflyEntity::new, SpawnGroup.AMBIENT)
            .dimensions(0.125F, 0.0625F)
            .maxTrackingRange(20)
            .build();

    public static final class Tags {
        public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("immune_to_sculk_jaw"));

        public static final TagKey<EntityType<?>> POISONS_FROG = TagKey.of(RegistryKeys.ENTITY_TYPE, Hollow.id("poisons_frog"));
    }
}
