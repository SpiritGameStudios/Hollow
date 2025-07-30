package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.block.HollowBlocks;
import dev.spiritstudios.hollow.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.component.HollowDataComponentTypes;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.item.HollowItems;
import dev.spiritstudios.hollow.loot.HollowLootTableModifications;
import dev.spiritstudios.hollow.loot.SetCopperInstrumentFunction;
import dev.spiritstudios.hollow.registry.HollowCriteria;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.hollow.registry.HollowRegistryKeys;
import dev.spiritstudios.hollow.sound.HollowSoundEvents;
import dev.spiritstudios.hollow.worldgen.HollowBiomeModifications;
import dev.spiritstudios.hollow.worldgen.feature.HollowFeatures;
import dev.spiritstudios.hollow.worldgen.tree.decorator.HollowTreeDecoratorTypes;
import dev.spiritstudios.hollow.worldgen.tree.foliage.HollowFoliagePlacerTypes;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final LootFunctionType<SetCopperInstrumentFunction> SET_COPPER_INSTRUMENT = new LootFunctionType<>(SetCopperInstrumentFunction.CODEC);

    @Override
    public void onInitialize() {
        HollowRegistryKeys.init();
        HollowSoundEvents.init();

        HollowBlocks.init();
        HollowItems.init();
        HollowEntityTypes.init();

        RegistryHelper.registerFields(Registries.FEATURE, RegistryHelper.fixGenerics(Feature.class), HollowFeatures.class, MODID);
        RegistryHelper.registerFields(Registries.TREE_DECORATOR_TYPE, RegistryHelper.fixGenerics(TreeDecoratorType.class), HollowTreeDecoratorTypes.class, MODID);
        RegistryHelper.registerBlockEntityTypes(HollowBlockEntityTypes.class, MODID);
        RegistryHelper.registerParticleTypes(HollowParticleTypes.class, MODID);
        RegistryHelper.registerDataComponentTypes(HollowDataComponentTypes.class, MODID);
        RegistryHelper.registerFields(Registries.FOLIAGE_PLACER_TYPE, RegistryHelper.fixGenerics(FoliagePlacerType.class), HollowFoliagePlacerTypes.class, MODID);
        RegistryHelper.registerFields(Registries.CRITERION, RegistryHelper.fixGenerics(Criterion.class), HollowCriteria.class, MODID);

        HollowGameRules.init();

        Registry.register(
                Registries.LOOT_FUNCTION_TYPE,
                id("set_copper_instrument"),
                SET_COPPER_INSTRUMENT
        );

        FabricDefaultAttributeRegistry.register(HollowEntityTypes.FIREFLY, FireflyEntity.createFireflyAttributes());

        HollowBiomeModifications.init();
        HollowLootTableModifications.init();
        HollowItemGroupAdditions.init();

        ComponentTooltipAppenderRegistry.addLast(HollowDataComponentTypes.COPPER_INSTRUMENT);
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
