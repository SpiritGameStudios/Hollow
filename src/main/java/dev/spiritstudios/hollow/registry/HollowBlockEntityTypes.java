package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.block.entity.EchoingPotBlockEntity;
import dev.spiritstudios.hollow.block.entity.JarBlockEntity;
import dev.spiritstudios.hollow.block.entity.StoneChestBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public final class HollowBlockEntityTypes {
    public static final BlockEntityType<JarBlockEntity> JAR_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, HollowBlocks.JAR).build();
    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(EchoingPotBlockEntity::new, HollowBlocks.ECHOING_POT).build();
    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(StoneChestBlockEntity::new, HollowBlocks.STONE_CHEST).build();
}
