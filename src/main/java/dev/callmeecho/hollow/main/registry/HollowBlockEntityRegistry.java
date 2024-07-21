package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import dev.callmeecho.hollow.main.block.JarBlock;
import dev.callmeecho.hollow.main.block.entity.EchoingPotBlockEntity;
import dev.callmeecho.hollow.main.block.entity.JarBlockEntity;
import dev.callmeecho.hollow.main.block.entity.StoneChestBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class HollowBlockEntityRegistry implements Registrar<BlockEntityType<?>> {
    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }
    
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCK_ENTITY = BlockEntityType.Builder.create(JarBlockEntity::new, HollowBlockRegistry.JAR).build();
    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT_BLOCK_ENTITY = BlockEntityType.Builder.create(EchoingPotBlockEntity::new, HollowBlockRegistry.ECHOING_POT).build();
    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY = BlockEntityType.Builder.create(StoneChestBlockEntity::new, HollowBlockRegistry.STONE_CHEST).build();
}
