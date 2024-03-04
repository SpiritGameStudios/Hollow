package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.EntityRegistrar;
import dev.callmeecho.hollow.main.entity.FireflyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class HollowEntityTypeRegistry implements EntityRegistrar {
    public static EntityType<FireflyEntity> FIREFLY = FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, FireflyEntity::new).dimensions(new EntityDimensions(0.125F, 0.0625F, true)).trackRangeBlocks(8).build();
}
