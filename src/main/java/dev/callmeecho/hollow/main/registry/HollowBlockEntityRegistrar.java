package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.BlockEntityTypeRegistrar;
import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.main.block.entity.EchoingPotBlockEntity;
import dev.callmeecho.hollow.main.block.entity.JarBlockEntity;
import dev.callmeecho.hollow.main.block.entity.StoneChestBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowBlockEntityRegistrar implements BlockEntityTypeRegistrar {
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCK_ENTITY = BlockEntityType.Builder.create(JarBlockEntity::new, HollowBlockRegistrar.JAR).build();
    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT_BLOCK_ENTITY = BlockEntityType.Builder.create(EchoingPotBlockEntity::new, HollowBlockRegistrar.ECHOING_POT).build();
    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY = BlockEntityType.Builder.create(StoneChestBlockEntity::new, HollowBlockRegistrar.STONE_CHEST).build();
}
