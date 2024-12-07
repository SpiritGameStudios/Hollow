package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.entity.FireflyEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

@SuppressWarnings("unused")
public final class HollowEntityTypes {
    public static final EntityType<FireflyEntity> FIREFLY = EntityType.Builder
            .create(FireflyEntity::new, SpawnGroup.AMBIENT)
            .dimensions(0.125F, 0.0625F)
            .maxTrackingRange(20)
            .build();
}
