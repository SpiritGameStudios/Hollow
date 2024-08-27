package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.registry.*;
import dev.spiritstudios.hollow.worldgen.HollowBiomeModifications;
import dev.spiritstudios.specter.api.config.ConfigManager;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final HollowConfig CONFIG = ConfigManager.getConfig(HollowConfig.class);

    @Override
    public void onInitialize() {
        Registrar.process(HollowSoundEventRegistrar.class, MODID);
        Registrar.process(HollowBlockRegistrar.class, MODID);
        Registrar.process(HollowItemRegistrar.class, MODID);
        Registrar.process(HollowEntityTypeRegistrar.class, MODID);
        Registrar.process(HollowFeatureRegistrar.class, MODID);
        Registrar.process(HollowTreeDecoratorRegistrar.class, MODID);
        Registrar.process(HollowBlockEntityRegistrar.class, MODID);
        Registrar.process(HollowParticleRegistrar.class, MODID);

        FabricDefaultAttributeRegistry.register(HollowEntityTypeRegistrar.FIREFLY, FireflyEntity.createFireflyAttributes());

        HollowBiomeModifications.init();
        HollowLootTableModifications.init();
    }
}
