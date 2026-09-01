package dev.spiritstudios.hollow.world.level.gen.feature;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.gen.tree.foliage.BlobWithHangingFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public final class HollowConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> CATTAIL = of("cattail");

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		context.register(
			CATTAIL,
			new ConfiguredFeature<>(
				Feature.SIMPLE_BLOCK,
				new SimpleBlockConfiguration(BlockStateProvider.simple(HollowBlocks.CATTAIL))
			)
		);
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Hollow.MODID, id));
	}

	public static TreeConfiguration.TreeConfigurationBuilder hangingLeavestreeBuilder(Block log, Block leaves, BlockStateProvider belowTrunkProvider, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius, float hangingLeavesChance, float hangingLeavesExtensionChance) {
		return new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(log),
			new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
			BlockStateProvider.simple(leaves),
			new BlobWithHangingFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3, hangingLeavesChance, hangingLeavesExtensionChance),
			new TwoLayersFeatureSize(1, 0, 1),
			belowTrunkProvider
		);
	}
}
