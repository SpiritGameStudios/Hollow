package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.data.component.HollowDataComponents;
import dev.spiritstudios.hollow.world.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.world.item.HollowItems;
import dev.spiritstudios.hollow.world.level.storage.loot.HollowLootFunctionTypes;
import dev.spiritstudios.hollow.world.level.storage.loot.HollowLootTableModifications;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.hollow.sound.HollowSoundEvents;
import dev.spiritstudios.hollow.world.level.gen.HollowBiomeModifications;
import dev.spiritstudios.hollow.world.level.gen.feature.HollowFeatures;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.HollowTreeDecoratorTypes;
import dev.spiritstudios.hollow.world.level.gen.tree.foliage.HollowFoliagePlacerTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        HollowRegistryKeys.init();
        HollowSoundEvents.init();

        HollowBlocks.init();
        HollowItems.init();
        HollowEntityTypes.init();

        HollowFeatures.init();
        HollowTreeDecoratorTypes.init();
        HollowBlockEntityTypes.init();
        HollowParticleTypes.init();
        HollowDataComponents.init();
        HollowFoliagePlacerTypes.init();

        HollowGameRules.init();

        HollowLootFunctionTypes.init();

        HollowBiomeModifications.init();
        HollowLootTableModifications.init();
        HollowItemGroupAdditions.init();

        ItemComponentTooltipProviderRegistry.addLast(HollowDataComponents.COPPER_INSTRUMENT);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
