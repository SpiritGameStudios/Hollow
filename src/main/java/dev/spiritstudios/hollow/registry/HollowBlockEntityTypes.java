package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.block.entity.EchoingPotBlockEntity;
import dev.spiritstudios.hollow.block.entity.EchoingVaseBlockEntity;
import dev.spiritstudios.hollow.block.entity.JarBlockEntity;
import dev.spiritstudios.hollow.block.entity.StoneChestBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public final class HollowBlockEntityTypes {
    public static final BlockEntityType<JarBlockEntity> JAR = BlockEntityType.Builder.create(JarBlockEntity::new, HollowBlocks.JAR).build();

    public static final BlockEntityType<EchoingVaseBlockEntity> ECHOING_VASE = BlockEntityType.Builder.create(EchoingVaseBlockEntity::new, HollowBlocks.ECHOING_VASE, HollowBlocks.SCREAMING_VASE).build();

    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT = BlockEntityType.Builder.create(EchoingPotBlockEntity::new, HollowBlocks.ECHOING_POT).build();

    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST = BlockEntityType.Builder.create(StoneChestBlockEntity::new, HollowBlocks.STONE_CHEST).build();
}
