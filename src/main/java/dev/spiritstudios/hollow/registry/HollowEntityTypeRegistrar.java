package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.specter.api.registry.registration.EntityTypeRegistrar;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

@SuppressWarnings("unused")
public class HollowEntityTypeRegistrar implements EntityTypeRegistrar {
    public static EntityType<FireflyEntity> FIREFLY = EntityType.Builder
            .create(FireflyEntity::new, SpawnGroup.AMBIENT)
            .dimensions(0.125F, 0.0625F).maxTrackingRange(8).build();
}
