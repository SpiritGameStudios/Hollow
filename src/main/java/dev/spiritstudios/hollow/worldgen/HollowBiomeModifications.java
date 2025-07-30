package dev.spiritstudios.hollow.worldgen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.HollowConfig;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.sound.HollowSoundEvents;
import dev.spiritstudios.hollow.worldgen.feature.HollowPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.sound.MusicType;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.function.Predicate;

public class HollowBiomeModifications {
    public static void init() {
        Predicate<BiomeSelectionContext> birch = BiomeSelectors.includeByKey(
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST
        );

        Predicate<BiomeSelectionContext> swamps = BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        );

        BiomeModifications.addSpawn(
                swamps,
                SpawnGroup.AMBIENT,
                HollowEntityTypes.FIREFLY,
                5,
                10, 15
        );

        SpawnRestriction.register(HollowEntityTypes.FIREFLY, SpawnRestriction.getLocation(HollowEntityTypes.FIREFLY), Heightmap.Type.WORLD_SURFACE, FireflyEntity::canSpawn);

        BiomeModifications.create(Hollow.id("hollow_swamp")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.SWAMP), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.HUGE_BROWN_MUSHROOM_SWAMP);
        });

        BiomeModifications.create(Hollow.id("hollow_swamps")).add(ModificationPhase.ADDITIONS, swamps, context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TWIG);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_GIANT_LILYPAD);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.CATTAILS);
        });

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.SWAMP),
                GenerationStep.Feature.VEGETAL_DECORATION,
                HollowPlacedFeatures.FALLEN_OAK
        );

        BiomeModifications.create(Hollow.id("hollow_birch")).add(ModificationPhase.ADDITIONS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (HollowConfig.INSTANCE.music.get())
                context.getEffects().setMusic(MusicType.createIngameMusic(HollowSoundEvents.MUSIC_OVERWORLD_BIRCH_FOREST));

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TWIG);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TALL_GRASS_BIRCH);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_CAMPION);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.FALLEN_BIRCH);
        }).add(ModificationPhase.REPLACEMENTS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (generationSettings.removeFeature(VegetationPlacedFeatures.PATCH_GRASS_FOREST))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_GRASS_BIRCH);
        }).add(ModificationPhase.REMOVALS, birch, context ->
                context.getGenerationSettings().removeFeature(VegetationPlacedFeatures.PATCH_PUMPKIN));
    }
}
