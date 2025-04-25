package dev.spiritstudios.hollow;

import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.doublekekse.area_lib.Area;
import dev.doublekekse.area_lib.AreaLib;
import dev.doublekekse.area_lib.command.argument.AreaArgument;
import dev.doublekekse.area_lib.component.AreaDataComponentType;
import dev.doublekekse.area_lib.registry.AreaDataComponentTypeRegistry;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.loot.HollowLootTableModifications;
import dev.spiritstudios.hollow.loot.SetCopperInstrumentFunction;
import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowCriteria;
import dev.spiritstudios.hollow.registry.HollowDataComponentTypes;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import dev.spiritstudios.hollow.registry.HollowFeatures;
import dev.spiritstudios.hollow.registry.HollowFoliagePlacerTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.hollow.registry.HollowSoundEvents;
import dev.spiritstudios.hollow.registry.HollowTreeDecoratorTypes;
import dev.spiritstudios.hollow.worldgen.HollowBiomeModifications;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public final class Hollow implements ModInitializer {
    public static final String MODID = "hollow";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final LootFunctionType<SetCopperInstrumentFunction> SET_COPPER_INSTRUMENT = new LootFunctionType<>(SetCopperInstrumentFunction.CODEC);

    public static final AreaDataComponentType<HollowShowcaseComponent> SHOWCASE = AreaDataComponentTypeRegistry.registerTracking(id("showcase"), HollowShowcaseComponent::new);

    @Override
    public void onInitialize() {
        RegistryHelper.registerSoundEvents(HollowSoundEvents.class, MODID);

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

        // all very stolen from meret

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("hollow")
                            .then(literal("set")
                                    .then(argument("area", AreaArgument.area())
                                            .then(argument("jars_enabled", BoolArgumentType.bool())
                                                    .then(argument("pots_enabled", BoolArgumentType.bool())
                                                            .executes(c -> set(
                                                                    c.getSource()::sendMessage,
                                                                    c.getSource().getServer(),
                                                                    AreaArgument.getArea(c, "area"),
                                                                    BoolArgumentType.getBool(c, "jars_enabled"),
                                                                    BoolArgumentType.getBool(c, "pots_enabled")
                                                            ))
                                                    )
                                            )
                                    )
                            ).then(literal("clear")
                                    .then(argument("area", AreaArgument.area())
                                            .executes(c -> clear(
                                                    c.getSource()::sendMessage,
                                                    c.getSource().getServer(),
                                                    AreaArgument.getArea(c, "area")
                                            ))
                                    )
                            )
                            .requires(source -> source.hasPermissionLevel(2))
            );
        });
    }

    private static int set(Consumer<Text> feedback, MinecraftServer server, Area area, boolean jarsEnabled, boolean potsEnabled) {
        area.put(server, SHOWCASE, new HollowShowcaseComponent(jarsEnabled, potsEnabled));
        feedback.accept(Text.of("Set showcase parameters successfully"));
        return 1;
    }

    private static int clear(Consumer<Text> feedback, MinecraftServer server, Area area) {
        if (!area.has(SHOWCASE)) feedback.accept(Text.of("Area does not contain a showcase component"));

        area.remove(server, SHOWCASE);
        feedback.accept(Text.of("Removed showcase component successfully"));

        return 1;
    }

    private static final HollowShowcaseComponent DEFAULT = new HollowShowcaseComponent();

    public static HollowShowcaseComponent getShowcase(World world, BlockPos pos) {
        return AreaLib.getSavedData(world)
                .findTrackedAreasContaining(world, new Vec3d(pos.getX(), pos.getY(), pos.getZ())).stream()
                .filter(area -> area.has(SHOWCASE))
                .max(Comparator.comparingInt(Area::getPriority))
                .map(area -> area.get(SHOWCASE))
                .orElse(DEFAULT);
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
