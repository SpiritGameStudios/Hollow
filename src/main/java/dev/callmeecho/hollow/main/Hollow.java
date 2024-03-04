package dev.callmeecho.hollow.main;

import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.entity.FireflyEntity;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import dev.callmeecho.hollow.main.registry.HollowEntityTypeRegistry;
import dev.callmeecho.hollow.main.registry.HollowItemRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final CabinetItemGroup GROUP = new CabinetItemGroup(new Identifier(MODID, "item_group"), HollowBlockRegistry.BIRCH_HOLLOW_LOG);

    @Override
    public void onInitialize() {
        RegistrarHandler.process(HollowBlockRegistry.class, MODID);
        RegistrarHandler.process(HollowItemRegistry.class, MODID);
        RegistrarHandler.process(HollowEntityTypeRegistry.class, MODID);
        FabricDefaultAttributeRegistry.register(HollowEntityTypeRegistry.FIREFLY, FireflyEntity.createFireflyAttributes());

        GROUP.initialize();
    }
}
