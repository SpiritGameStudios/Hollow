package dev.spiritstudios.hollow.world.level.gen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.gen.feature.HollowPlacements;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.Biomes;

import java.util.function.Predicate;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION;

public class HollowBiomeModifications {
    public static void init() {
        Predicate<BiomeSelectionContext> birch = BiomeSelectors.includeByKey(
                Biomes.BIRCH_FOREST,
                Biomes.OLD_GROWTH_BIRCH_FOREST
        );

        Predicate<BiomeSelectionContext> swamps = BiomeSelectors.includeByKey(
                Biomes.SWAMP,
                Biomes.MANGROVE_SWAMP
        );

        BiomeModifications.create(Hollow.id("swamp")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(Biomes.SWAMP), context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.HUGE_BROWN_MUSHROOM_SWAMP);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.HUGE_RED_MUSHROOM_SWAMP);
        });

        BiomeModifications.create(Hollow.id("swamps")).add(ModificationPhase.ADDITIONS, swamps, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.PATCH_GIANT_LILY_PAD);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.CATTAILS);
        });


        BiomeModifications.create(Hollow.id("birch")).add(ModificationPhase.ADDITIONS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

//            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.PATCH_TALL_GRASS_BIRCH);
//            settings.addFeature(VEGETAL_DECORATION, HollowPlacements.FALLEN_BIRCH);
        }).add(ModificationPhase.REPLACEMENTS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

//            if (settings.removeFeature(VegetationPlacements.PATCH_GRASS_FOREST))
//                settings.addFeature(VEGETAL_DECORATION, HollowPlacements.PATCH_GRASS_BIRCH);
        }).add(ModificationPhase.REMOVALS, birch, context ->
                context.getGenerationSettings().removeFeature(VegetationPlacements.PATCH_PUMPKIN));
    }
}
