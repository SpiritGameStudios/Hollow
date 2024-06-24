package dev.callmeecho.hollow.main;

import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.entity.FireflyEntity;
import dev.callmeecho.hollow.main.registry.*;
import dev.callmeecho.hollow.main.worldgen.HollowBiomeModifications;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
    
    public static final CabinetItemGroup GROUP = new CabinetItemGroup(Identifier.of(MODID, "item_group"), HollowBlockRegistry.BIRCH_HOLLOW_LOG);

    @Override
    public void onInitialize() {
        RegistrarHandler.process(HollowSoundEventRegistry.class, MODID);
        RegistrarHandler.process(HollowBlockRegistry.class, MODID);
        RegistrarHandler.process(HollowItemRegistry.class, MODID);
        RegistrarHandler.process(HollowEntityTypeRegistry.class, MODID);
        RegistrarHandler.process(HollowFeatureRegistry.class, MODID);
        RegistrarHandler.process(HollowTreeDecoratorRegistry.class, MODID);
        RegistrarHandler.process(HollowBlockEntityRegistry.class, MODID);
        RegistrarHandler.process(HollowParticleRegistrar.class, MODID);

        FabricDefaultAttributeRegistry.register(HollowEntityTypeRegistry.FIREFLY, FireflyEntity.createFireflyAttributes());
    
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
