package dev.spiritstudios.hollow.world.level.block.entity;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;

public final class HollowBlockEntityTypes {
    public static final BlockEntityType<JarBlockEntity> JAR = register("jar", JarBlockEntity::new, HollowBlocks.JAR);

    public static final BlockEntityType<EchoingVaseBlockEntity> ECHOING_VASE = register(
            "echoing_vase",
            EchoingVaseBlockEntity::new,
            HollowBlocks.ECHOING_VASE, HollowBlocks.OBABO, HollowBlocks.SCREAMING_VASE
    );

    public static final BlockEntityType<EchoingPotBlockEntity> ECHOING_POT = register("echoing_pot", EchoingPotBlockEntity::new, HollowBlocks.ECHOING_POT);

    public static final BlockEntityType<StoneChestBlockEntity> STONE_CHEST = register("stone_chest", StoneChestBlockEntity::new, HollowBlocks.STONE_CHEST);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory,
            Block... validBlocks
    ) {
        return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Hollow.id(name),
                FabricBlockEntityTypeBuilder.create(factory, validBlocks).build()
        );
    }

    public static void init() {
        // NO-OP
    }
}