package dev.spiritstudios.hollow.worldgen.feature;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.hollow.registry.HollowFeatureRegistrar;
import dev.spiritstudios.hollow.worldgen.decorator.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.BranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.PolyporeTreeDecorator;
import dev.spiritstudios.hollow.worldgen.foliage.BlobWithHangingFoliagePlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public final class HollowConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_OAK = of("fallen_oak");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH = of("fallen_birch");

    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_TWIG = of("patch_twig");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_CAMPION = of("patch_campion");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_GIANT_LILYPAD = of("patch_giant_lilypad");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_WATERLILY = of("patch_waterlily");


    public static final RegistryKey<ConfiguredFeature<?, ?>> SWAMP_OAK = of("swamp_oak");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002 = of("birch_bees_0002");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SUPER_BIRCH_BEES_0002 = of("super_birch_bees_0002");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BIRCH_TALL = of("birch_tall");


    public static final RegistryKey<ConfiguredFeature<?, ?>> CATTAILS = of("cattails");


    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        RegistryEntryLookup<PlacedFeature> placedLookup = featureRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        featureRegisterable.register(FALLEN_OAK, fallenTree(HollowBlockRegistrar.OAK_HOLLOW_LOG, false, true));
        featureRegisterable.register(FALLEN_BIRCH, fallenTree(HollowBlockRegistrar.BIRCH_HOLLOW_LOG, true, true));

        featureRegisterable.register(PATCH_TWIG, createRandomPatch(HollowBlockRegistrar.TWIG, 10));
        featureRegisterable.register(PATCH_CAMPION, createRandomPatch(HollowBlockRegistrar.CAMPION, 10));
        featureRegisterable.register(
                PATCH_GIANT_LILYPAD,
                createRandomPatch(
                        PlacedFeatures.createEntry(HollowFeatureRegistrar.GIANT_LILYPAD, new DefaultFeatureConfig()),
                        10
                )
        );

        // region Trees
        featureRegisterable.register(
                SWAMP_OAK,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.OAK_LOG, Blocks.OAK_LEAVES,
                                8, 2, 0,
                                3, 1.0F, 0.5F
                        ).decorators(List.of(
                                new LeavesVineTreeDecorator(0.05F),
                                new BigBranchTreeDecorator(BlockStateProvider.of(Blocks.OAK_LOG), 0.5F)
                        )).build()
                )
        );

        featureRegisterable.register(
                BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES,
                                8, 5, 0,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                new PolyporeTreeDecorator(BlockStateProvider.of(HollowBlockRegistrar.POLYPORE)),
                                new BranchTreeDecorator(BlockStateProvider.of(Blocks.BIRCH_LOG), 0.5F, 2)
                        )).ignoreVines().build()
                )
        );

        featureRegisterable.register(
                SUPER_BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES,
                                8, 5, 6,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                new PolyporeTreeDecorator(BlockStateProvider.of(HollowBlockRegistrar.POLYPORE)),
                                new BranchTreeDecorator(BlockStateProvider.of(Blocks.BIRCH_LOG), 0.5F, 5)
                        )).ignoreVines().build()
                )
        );

        featureRegisterable.register(
                BIRCH_TALL,
                new ConfiguredFeature<>(
                        Feature.RANDOM_SELECTOR,
                        new RandomFeatureConfig(
                                List.of(
                                        new RandomFeatureEntry(
                                                placedLookup.getOrThrow(HollowPlacedFeatures.SUPER_BIRCH_BEES_0002),
                                                0.5F
                                        )
                                ),
                                placedLookup.getOrThrow(HollowPlacedFeatures.BIRCH_BEES_0002)
                        )
                )
        );
        // endregion

        featureRegisterable.register(
                CATTAILS,
                new ConfiguredFeature<>(
                        HollowFeatureRegistrar.CATTAILS,
                        new DefaultFeatureConfig()
                )
        );

        featureRegisterable.register(
                PATCH_WATERLILY,
                createRandomPatch(
                        new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                                .add(Blocks.LILY_PAD.getDefaultState(), 4)
                                .add(HollowBlockRegistrar.LOTUS_LILYPAD.getDefaultState(), 1)
                                .build()),
                        10
                )
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Hollow.MODID, id));
    }


    public static ConfiguredFeature<?, ?> fallenTree(Block block, boolean polypore, boolean mossy) {
        return new ConfiguredFeature<>(
                HollowFeatureRegistrar.FALLEN_TREE,
                new FallenTreeFeatureConfig(BlockStateProvider.of(block), polypore, mossy)
        );
    }

    public static ConfiguredFeature<?, ?> createRandomPatch(RegistryEntry<PlacedFeature> feature, int tries) {
        return new ConfiguredFeature<>(
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(
                        tries,
                        feature
                )
        );
    }

    public static ConfiguredFeature<?, ?> createRandomPatch(BlockStateProvider block, int tries) {
        return createRandomPatch(
                PlacedFeatures.createEntry(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(block)
                ),
                tries
        );

    }

    private static ConfiguredFeature<?, ?> createRandomPatch(Block block, int tries) {
        return createRandomPatch(BlockStateProvider.of(block), tries);
    }

    private static TreeFeatureConfig.Builder treeBuilder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius) {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(log),
                new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
                BlockStateProvider.of(leaves),
                new BlobFoliagePlacer(ConstantIntProvider.create(radius), ConstantIntProvider.create(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }

    private static TreeFeatureConfig.Builder hangingLeavestreeBuilder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius, float hangingLeavesChance, float hangingLeavesExtensionChance) {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(log),
                new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight),
                BlockStateProvider.of(leaves),
                new BlobWithHangingFoliagePlacer(ConstantIntProvider.create(radius), ConstantIntProvider.create(0), 3, hangingLeavesChance, hangingLeavesExtensionChance),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }
}