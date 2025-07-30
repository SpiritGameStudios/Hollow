package dev.spiritstudios.hollow.block.entity;

import dev.spiritstudios.hollow.block.HollowBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public final class HollowBlockEntityTypes {
    public static final BlockEntityType<JarBlockEntity> JAR = FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, HollowBlocks.JAR).build();

    public static final BlockEntityType<EchoingVaseBlockEntity> ECHOING_VASE = FabricBlockEntityTypeBuilder.create(EchoingVaseBlockEntity::new, HollowBlocks.ECHOING_VASE, HollowBlocks.OBABO, HollowBlocks.SCREAMING_VASE).build();

    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT = FabricBlockEntityTypeBuilder.create(EchoingPotBlockEntity::new, HollowBlocks.ECHOING_POT).build();

    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST = FabricBlockEntityTypeBuilder.create(StoneChestBlockEntity::new, HollowBlocks.STONE_CHEST).build();
}
