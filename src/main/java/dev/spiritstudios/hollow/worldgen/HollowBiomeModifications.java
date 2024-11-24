package dev.spiritstudios.hollow.worldgen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.HollowConfig;
import dev.spiritstudios.hollow.entity.FireflyEntity;
import dev.spiritstudios.hollow.registry.HollowEntityTypeRegistrar;
import dev.spiritstudios.hollow.registry.HollowSoundEventRegistrar;
import dev.spiritstudios.hollow.worldgen.feature.HollowPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.*;
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
                HollowEntityTypeRegistrar.FIREFLY,
                5,
                10, 15
        );

        SpawnRestriction.register(HollowEntityTypeRegistrar.FIREFLY, SpawnRestriction.getLocation(HollowEntityTypeRegistrar.FIREFLY), Heightmap.Type.WORLD_SURFACE, FireflyEntity::canSpawn);

        BiomeModifications.create(Identifier.of(Hollow.MODID, "hollow_swamp")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.SWAMP), context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();
            if (HollowConfig.INSTANCE.music.get())
                context.getEffects().setMusic(new MusicSound(
                        Registries.SOUND_EVENT.getEntry(HollowSoundEventRegistrar.MUSIC_SWAMP),
                        12000,
                        24000,
                        false
                ));

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.HUGE_BROWN_MUSHROOM_SWAMP);
        });

        BiomeModifications.create(Identifier.of(Hollow.MODID, "hollow_swamps")).add(ModificationPhase.ADDITIONS, swamps, context -> {
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

        BiomeModifications.create(Identifier.of(Hollow.MODID, "hollow_birch")).add(ModificationPhase.ADDITIONS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (HollowConfig.INSTANCE.music.get())
                context.getEffects().setMusic(new MusicSound(
                        Registries.SOUND_EVENT.getEntry(HollowSoundEventRegistrar.MUSIC_BIRCH_FOREST),
                        12000,
                        24000,
                        false
                ));

            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TWIG);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TALL_GRASS_BIRCH);
            generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_CAMPION);
        }).add(ModificationPhase.REPLACEMENTS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

            if (generationSettings.removeFeature(VegetationPlacedFeatures.PATCH_GRASS_FOREST))
                generationSettings.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_GRASS_BIRCH);
        }).add(ModificationPhase.REMOVALS, birch, context ->
                context.getGenerationSettings().removeFeature(VegetationPlacedFeatures.PATCH_PUMPKIN));

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
}
