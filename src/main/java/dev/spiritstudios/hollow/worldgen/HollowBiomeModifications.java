package dev.spiritstudios.hollow.worldgen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.HollowConfig;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.registry.HollowEntityTypeRegistrar;
import dev.spiritstudios.hollow.registry.HollowSoundEventRegistrar;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

public class HollowBiomeModifications {
    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        ), SpawnGroup.AMBIENT, HollowEntityTypeRegistrar.FIREFLY, 5, 10, 15);
        SpawnRestriction.register(HollowEntityTypeRegistrar.FIREFLY, SpawnRestriction.getLocation(HollowEntityTypeRegistrar.FIREFLY), Heightmap.Type.WORLD_SURFACE, FireflyEntity::canSpawn);

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_swamps_replace")).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (generationSettings.removeFeature(VegetationPlacedFeatures.PATCH_WATERLILY))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_waterlily"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_swamp_replace")).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (generationSettings.removeFeature(VegetationPlacedFeatures.TREES_SWAMP))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("trees_swamp"));

            if (generationSettings.removeFeature(VegetationPlacedFeatures.FLOWER_SWAMP))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("swamp_flowers"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_swamp_add")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();
            if (HollowConfig.INSTANCE.music.get())
                context.getEffects().setMusic(new MusicSound(
                        Registries.SOUND_EVENT.getEntry(HollowSoundEventRegistrar.MUSIC_SWAMP),
                        12000,
                        24000,
                        false
                ));

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("fallen_oak"));
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("huge_brown_mushroom"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_oak_add")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(
                BiomeKeys.FOREST,
                BiomeKeys.FLOWER_FOREST
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("fallen_oak"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_swamps_add")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_twig"));
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_giant_lilypad"));
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("cattails"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_birch_add")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();
            BiomeModificationContext.EffectsContext effects = context.getEffects();

            if (HollowConfig.INSTANCE.music.get())
                effects.setMusic(new MusicSound(
                        Registries.SOUND_EVENT.getEntry(HollowSoundEventRegistrar.MUSIC_BIRCH_FOREST),
                        12000,
                        24000,
                        false
                ));

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("fallen_birch"));
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_twig"));
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_tall_grass_birch"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_birch_replace")).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();
            if (generationSettings.removeFeature(VegetationPlacedFeatures.TREES_BIRCH))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("trees_birch"));

            if (generationSettings.removeFeature(VegetationPlacedFeatures.BIRCH_TALL))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("birch_tall"));

            if (generationSettings.removeFeature(VegetationPlacedFeatures.FLOWER_DEFAULT) && generationSettings.removeFeature(VegetationPlacedFeatures.FOREST_FLOWERS)) {
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_campion"));
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("birch_forest_flowers"));
            }

            if (generationSettings.removeFeature(VegetationPlacedFeatures.PATCH_GRASS_FOREST))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, getPlacedFeature("patch_grass_birch"));
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "better_birch_remove")).add(ModificationPhase.REMOVALS, BiomeSelectors.includeByKey(
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST
        ), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            generationSettings.removeFeature(VegetationPlacedFeatures.PATCH_PUMPKIN);
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "deep_dark_music")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(
                BiomeKeys.DEEP_DARK
        ), context -> {
            if (HollowConfig.INSTANCE.music.get())
                context.getEffects().setMusic(new MusicSound(
                        Registries.SOUND_EVENT.getEntry(HollowSoundEventRegistrar.MUSIC_DEEP_DARK),
                        12000,
                        24000,
                        false
                ));
        });
    }

    public static RegistryKey<PlacedFeature> getPlacedFeature(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Hollow.MODID, id));
    }
}
