package dev.callmeecho.hollow.main;

import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.entity.FireflyEntity;
import dev.callmeecho.hollow.main.registry.*;
import dev.callmeecho.hollow.main.worldgen.HollowBiomeModifications;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    
    public static final CabinetItemGroup GROUP = new CabinetItemGroup(Identifier.of(MODID, "item_group"), HollowBlockRegistrar.BIRCH_HOLLOW_LOG);

    @Override
    public void onInitialize() {
        RegistrarHandler.process(HollowSoundEventRegistrar.class, MODID);
        RegistrarHandler.process(HollowBlockRegistrar.class, MODID);
        RegistrarHandler.process(HollowItemRegistrar.class, MODID);
        RegistrarHandler.process(HollowEntityTypeRegistrar.class, MODID);
        RegistrarHandler.process(HollowFeatureRegistrar.class, MODID);
        RegistrarHandler.process(HollowTreeDecoratorRegistrar.class, MODID);
        RegistrarHandler.process(HollowBlockEntityRegistrar.class, MODID);
        RegistrarHandler.process(HollowParticleRegistrar.class, MODID);

        FabricDefaultAttributeRegistry.register(HollowEntityTypeRegistrar.FIREFLY, FireflyEntity.createFireflyAttributes());

        OxidizableBlocksRegistry.registerOxidizableBlockPair(HollowBlockRegistrar.COPPER_PILLAR, HollowBlockRegistrar.EXPOSED_COPPER_PILLAR);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(HollowBlockRegistrar.EXPOSED_COPPER_PILLAR, HollowBlockRegistrar.WEATHERED_COPPER_PILLAR);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(HollowBlockRegistrar.WEATHERED_COPPER_PILLAR, HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR);

        OxidizableBlocksRegistry.registerWaxableBlockPair(HollowBlockRegistrar.COPPER_PILLAR, HollowBlockRegistrar.WAXED_COPPER_PILLAR);
        OxidizableBlocksRegistry.registerWaxableBlockPair(HollowBlockRegistrar.EXPOSED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_EXPOSED_COPPER_PILLAR);
        OxidizableBlocksRegistry.registerWaxableBlockPair(HollowBlockRegistrar.WEATHERED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_WEATHERED_COPPER_PILLAR);
        OxidizableBlocksRegistry.registerWaxableBlockPair(HollowBlockRegistrar.OXIDIZED_COPPER_PILLAR, HollowBlockRegistrar.WAXED_OXIDIZED_COPPER_PILLAR);

        HollowBiomeModifications.init();
        HollowLootTableModifications.init();
        
        GROUP.initialize();

        FabricLoader.getInstance().getModContainer(MODID).ifPresent(modContainer ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                    Identifier.of(MODID, "hollow_music"),
                    modContainer,
                    Text.translatable("resourcepack.hollow_music"),
                    ResourcePackActivationType.DEFAULT_ENABLED));
    }
}
