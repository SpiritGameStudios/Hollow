package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import dev.spiritstudios.hollow.core.registry.HollowRegistries;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import dev.spiritstudios.hollow.world.item.HollowCreativeModeTab;
import dev.spiritstudios.hollow.world.item.HollowItems;
import dev.spiritstudios.hollow.world.level.HollowGameRules;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.gen.HollowBiomeModifications;
import dev.spiritstudios.hollow.world.level.gen.feature.HollowFeatures;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.HollowTreeDecoratorTypes;
import dev.spiritstudios.hollow.world.level.gen.tree.foliage.HollowFoliagePlacerTypes;
import dev.spiritstudios.hollow.world.level.storage.loot.HollowLootFunctionTypes;
import dev.spiritstudios.hollow.world.level.storage.loot.HollowLootTableModifications;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        HollowRegistries.init();
        HollowSoundEvents.init();

        HollowBlocks.init();
		HollowDataComponents.init();
        HollowItems.init();

        HollowFeatures.init();
        HollowTreeDecoratorTypes.init();
        HollowBlockEntityTypes.init();
        HollowFoliagePlacerTypes.init();
		HollowBiomeModifications.init();

		HollowLootFunctionTypes.init();
		HollowLootTableModifications.init();

		HollowGameRules.init();

		HollowParticleTypes.init();
		HollowCreativeModeTab.init();

		addAliases();
    }

	private static void addAliases() {
		Identifier jar = id("jar"), glassJar = HollowBlockItemIds.GLASS_JAR.block().identifier();
		Identifier jarOfFireflies = id("jar_of_fireflies"), fireflyJar = HollowBlockItemIds.FIREFLY_JAR.block().identifier();

		BuiltInRegistries.BLOCK.addAlias(jar, glassJar);
		BuiltInRegistries.BLOCK.addAlias(jarOfFireflies, fireflyJar);

		BuiltInRegistries.ITEM.addAlias(jar, glassJar);
		BuiltInRegistries.ITEM.addAlias(jarOfFireflies, fireflyJar);

		BuiltInRegistries.BLOCK_ENTITY_TYPE.addAlias(jar, glassJar);
	}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
