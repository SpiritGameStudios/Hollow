package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.gen.tree.foliage.BlobWithHangingFoliagePlacer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public final class HollowConfiguredFeatures {
	public static final ResourceKey<Feature> CATTAIL = of("cattail");

	public static void bootstrap(BootstrapContext<Feature> context) {
		context.register(
			CATTAIL,
			new SimpleBlockFeature(BlockStateProvider.holderOf(HollowBlocks.CATTAIL))
		);
	}

	public static ResourceKey<Feature> of(String id) {
		return ResourceKey.create(Registries.FEATURE, Hollow.id(id));
	}

	public static TreeFeature.Builder hangingLeavestreeBuilder(Block log, Block leaves, Holder<BlockStateProvider> belowTrunkProvider, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius, float hangingLeavesChance, float hangingLeavesExtensionChance) {
		return new TreeFeature.Builder(
			BlockStateProvider.of(log),
			new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
			BlockStateProvider.of(leaves),
			new BlobWithHangingFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3, hangingLeavesChance, hangingLeavesExtensionChance),
			new TwoLayersFeatureSize(1, 0, 1),
			belowTrunkProvider
		);
	}
}
