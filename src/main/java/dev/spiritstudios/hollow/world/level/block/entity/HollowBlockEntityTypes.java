package dev.spiritstudios.hollow.world.level.block.entity;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.entity.pot.FallingPotBlockEntity;
import dev.spiritstudios.hollow.world.level.block.entity.pot.PotBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class HollowBlockEntityTypes {
    public static final BlockEntityType<GlassJarBlockEntity> GLASS_JAR = register("glass_jar", GlassJarBlockEntity::new, HollowBlocks.GLASS_JAR);
	public static final BlockEntityType<FireflyJarBlockEntity> FIREFLY_JAR = register("firefly_jar", FireflyJarBlockEntity::new, HollowBlocks.FIREFLY_JAR);

    public static final BlockEntityType<FallingPotBlockEntity> FALLING_POT = register(
            "falling_pot",
            FallingPotBlockEntity::new,
            HollowBlocks.SCREAMING_VASE
    );

    public static final BlockEntityType<PotBlockEntity> POT = register(
		"pot",
		PotBlockEntity::new,
		HollowBlocks.ECHOING_POT, HollowBlocks.ECHOING_VASE, HollowBlocks.OBABO
	);

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
