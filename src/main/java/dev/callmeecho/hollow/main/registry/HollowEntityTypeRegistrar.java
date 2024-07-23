package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.EntityTypeRegistrar;
import dev.callmeecho.hollow.main.entity.FireflyEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

@SuppressWarnings("unused")
public class HollowEntityTypeRegistrar implements EntityTypeRegistrar {
    public static EntityType<FireflyEntity> FIREFLY = EntityType.Builder
            .create(FireflyEntity::new, SpawnGroup.AMBIENT)
            .dimensions(0.125F, 0.0625F).maxTrackingRange(8).build();
}
