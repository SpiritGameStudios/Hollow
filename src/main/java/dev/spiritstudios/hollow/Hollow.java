package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.loot.HollowLootTableModifications;
import dev.spiritstudios.hollow.loot.SetCopperInstrumentFunction;
import dev.spiritstudios.hollow.registry.*;
import dev.spiritstudios.hollow.worldgen.HollowBiomeModifications;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final LootFunctionType<SetCopperInstrumentFunction> SET_COPPER_INSTRUMENT = new LootFunctionType<>(SetCopperInstrumentFunction.CODEC);

    @Override
    public void onInitialize() {
        Registrar.process(HollowSoundEvents.class, MODID);
        Registrar.process(HollowBlocks.class, MODID);
        Registrar.process(HollowItems.class, MODID);
        Registrar.process(HollowEntityTypes.class, MODID);
        Registrar.process(HollowFeatures.class, MODID);
        Registrar.process(HollowTreeDecorators.class, MODID);
        Registrar.process(HollowBlockEntityTypes.class, MODID);
        Registrar.process(HollowParticleTypes.class, MODID);
        Registrar.process(HollowDataComponentTypes.class, MODID);
        Registrar.process(HollowFoliagePlacerTypes.class, MODID);

        HollowGameRules.init();

        Registry.register(
                Registries.LOOT_FUNCTION_TYPE,
                id("set_copper_instrument"),
                SET_COPPER_INSTRUMENT
        );

        List.of(
                "call",
                "melody",
                "bass"
        ).forEach(name -> {
            for (int i = 0; i < 10; i++) {
                Identifier id = id("horn.%s.%d".formatted(name, i));
                Registry.register(
                        Registries.SOUND_EVENT,
                        id,
                        SoundEvent.of(id)
                );
            }
        });

        FabricDefaultAttributeRegistry.register(HollowEntityTypes.FIREFLY, FireflyEntity.createFireflyAttributes());

        HollowBiomeModifications.init();
        HollowLootTableModifications.init();
        HollowItemGroupAdditions.init();
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
