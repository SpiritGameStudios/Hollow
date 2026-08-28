package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public final class HollowPlacements {
	public static final ResourceKey<PlacedFeature> PATCH_CATTAIL = of("patch_cattail");

	public static final ResourceKey<PlacedFeature> PATCH_GIANT_LILY_PAD = of("patch_giant_lily_pad");
//    public static final ResourceKey<PlacedFeature> PATCH_GRASS_BIRCH = of("patch_grass_birch");
//    public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS_BIRCH = of("patch_tall_grass_birch");

	public static final ResourceKey<PlacedFeature> HUGE_BROWN_MUSHROOM_SWAMP = of("huge_brown_mushroom_swamp");
	public static final ResourceKey<PlacedFeature> HUGE_RED_MUSHROOM_SWAMP = of("huge_red_mushroom_swamp");

	// Basically checks if there is a non water block in any of the adjacent 8 blocks
	private static final BlockPredicate NEAR_COAST = BlockPredicate.anyOf(
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(1, 0, 1), Fluids.WATER, Fluids.FLOWING_WATER)),
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(-1, 0, 1), Fluids.WATER, Fluids.FLOWING_WATER)),
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(0, 0, 1), Fluids.WATER, Fluids.FLOWING_WATER)),

		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(1, 0, -1), Fluids.WATER, Fluids.FLOWING_WATER)),
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(-1, 0, -1), Fluids.WATER, Fluids.FLOWING_WATER)),
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(0, 0, -1), Fluids.WATER, Fluids.FLOWING_WATER)),

		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(1, 0, 0), Fluids.WATER, Fluids.FLOWING_WATER)),
		BlockPredicate.not(BlockPredicate.matchesFluids(new Vec3i(-1, 0, 0), Fluids.WATER, Fluids.FLOWING_WATER))
	);

	public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
		PlacedFeatureHelper helper = new PlacedFeatureHelper(featureRegisterable.lookup(Registries.CONFIGURED_FEATURE), featureRegisterable);

//        helper.add(
//                PATCH_GRASS_BIRCH,
//                VegetationFeatures.PATCH_GRASS,
//                CountPlacement.of(20),
//                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
//                BiomeFilter.biome(),
//                InSquarePlacement.spread()
//        );
//
//        helper.add(
//                PATCH_TALL_GRASS_BIRCH,
//                VegetationFeatures.PATCH_TALL_GRASS,
//                CountPlacement.of(1),
//                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
//                BiomeFilter.biome(),
//                InSquarePlacement.spread()
//        );

		helper.add(
			HUGE_BROWN_MUSHROOM_SWAMP,
			TreeFeatures.HUGE_BROWN_MUSHROOM,
			CountPlacement.of(1),
			RarityFilter.onAverageOnceEvery(2),
			HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
			BiomeFilter.biome(),
			InSquarePlacement.spread()
		);

		helper.add(
			HUGE_RED_MUSHROOM_SWAMP,
			TreeFeatures.HUGE_RED_MUSHROOM,
			CountPlacement.of(1),
			RarityFilter.onAverageOnceEvery(2),
			HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
			BiomeFilter.biome(),
			InSquarePlacement.spread()
		);

		helper.add(
			PATCH_CATTAIL,
			HollowConfiguredFeatures.CATTAIL,
			CountPlacement.of(96),
			HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
			BiomeFilter.biome(),
			RandomOffsetPlacement.ofTriangle(3, 3),
			BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.WATER),
				NEAR_COAST
			))
		);

		helper.add(
			PATCH_GIANT_LILY_PAD,
			HollowConfiguredFeatures.GIANT_LILY_PAD,
			HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
			BiomeFilter.biome(),
			InSquarePlacement.spread(),
			CountPlacement.of(10),
			RandomOffsetPlacement.ofTriangle(7, 3),
			BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
		);
	}

	public static ResourceKey<PlacedFeature> of(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Hollow.MODID, id));
	}

	private record PlacedFeatureHelper(HolderGetter<ConfiguredFeature<?, ?>> lookup,
	                                   BootstrapContext<PlacedFeature> featureRegisterable) {
		public void add(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
			featureRegisterable.register(
				key,
				new PlacedFeature(
					lookup.getOrThrow(configuredKey),
					List.of(modifiers)
				)
			);
		}
	}
}
