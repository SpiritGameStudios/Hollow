package dev.callmeecho.hollow.main;

import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class Hollow implements ModInitializer {
    public static final String MODID = "hollow";

    public static final CabinetItemGroup GROUP = new CabinetItemGroup(new Identifier(MODID, "item_group"), HollowBlockRegistry.BIRCH_HOLLOW_LOG);
    
    @Override
    public void onInitialize() {
        RegistrarHandler.process(HollowBlockRegistry.class, MODID);

        GROUP.initialize();
    }
}
