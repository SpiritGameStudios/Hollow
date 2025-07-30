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
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

import java.util.function.Predicate;

import static net.minecraft.world.gen.GenerationStep.Feature.VEGETAL_DECORATION;

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

        BiomeModifications.create(Hollow.id("swamp")).add(ModificationPhase.ADDITIONS, BiomeSelectors.includeByKey(BiomeKeys.SWAMP), context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.HUGE_BROWN_MUSHROOM_SWAMP);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.HUGE_RED_MUSHROOM_SWAMP);
        });

        BiomeModifications.create(Hollow.id("swamps")).add(ModificationPhase.ADDITIONS, swamps, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TWIG);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_GIANT_LILYPAD);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.CATTAILS);
        });

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.SWAMP),
                VEGETAL_DECORATION,
                HollowPlacedFeatures.FALLEN_OAK
        );

        BiomeModifications.create(Hollow.id("birch")).add(ModificationPhase.ADDITIONS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            if (HollowConfig.INSTANCE.music.get()) {
				context.getEffects().setMusic(MusicType.createIngameMusic(HollowSoundEvents.MUSIC_OVERWORLD_BIRCH_FOREST));
			}

            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TWIG);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_TALL_GRASS_BIRCH);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_CAMPION);
            settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.FALLEN_BIRCH);
        }).add(ModificationPhase.REPLACEMENTS, birch, context -> {
            BiomeModificationContext.GenerationSettingsContext settings = context.getGenerationSettings();

            if (settings.removeFeature(VegetationPlacedFeatures.PATCH_GRASS_FOREST))
                settings.addFeature(VEGETAL_DECORATION, HollowPlacedFeatures.PATCH_GRASS_BIRCH);
        }).add(ModificationPhase.REMOVALS, birch, context ->
                context.getGenerationSettings().removeFeature(VegetationPlacedFeatures.PATCH_PUMPKIN));
    }
}
