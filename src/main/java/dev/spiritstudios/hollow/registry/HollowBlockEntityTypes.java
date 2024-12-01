package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.block.entity.EchoingPotBlockEntity;
import dev.spiritstudios.hollow.block.entity.JarBlockEntity;
import dev.spiritstudios.hollow.block.entity.StoneChestBlockEntity;
import dev.spiritstudios.specter.api.registry.registration.BlockEntityTypeRegistrar;
import net.minecraft.block.entity.BlockEntityType;

public class HollowBlockEntityTypes implements BlockEntityTypeRegistrar {
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCK_ENTITY = BlockEntityType.Builder.create(JarBlockEntity::new, HollowBlocks.JAR).build();
    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT_BLOCK_ENTITY = BlockEntityType.Builder.create(EchoingPotBlockEntity::new, HollowBlocks.ECHOING_POT).build();
    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY = BlockEntityType.Builder.create(StoneChestBlockEntity::new, HollowBlocks.STONE_CHEST).build();
}
