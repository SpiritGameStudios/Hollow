package dev.spiritstudios.hollow.data.gen;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.PolyporeBlock;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.BranchTreeDecorator;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.PolyporeTreeDecorator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLogsDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.spiritstudios.hollow.world.level.gen.feature.HollowConfiguredFeatures.hangingLeavestreeBuilder;

public class HollowConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public HollowConfiguredFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        HolderLookup<ConfiguredFeature<?, ?>> lookup = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        HolderLookup<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));

        BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);

        PolyporeTreeDecorator polyporeTreeDecorator = new PolyporeTreeDecorator(
                new RandomizedIntStateProvider(
                        BlockStateProvider.simple(HollowBlocks.POLYPORE),
                        PolyporeBlock.POLYPORE_AMOUNT,
                        UniformInt.of(1, 3)
                )
        );

        entries.add(
                TreeFeatures.BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, belowTrunkProvider,
                                8, 5, 0,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                polyporeTreeDecorator,
                                new BranchTreeDecorator(BlockStateProvider.simple(Blocks.BIRCH_LOG), 0.5F, 2)
                        )).ignoreVines().build()
                )
        );

        entries.add(
                TreeFeatures.SUPER_BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, belowTrunkProvider,
                                8, 5, 6,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                polyporeTreeDecorator,
                                new BranchTreeDecorator(BlockStateProvider.simple(Blocks.BIRCH_LOG), 0.5F, 5)
                        )).ignoreVines().build()
                )
        );

        entries.add(
                TreeFeatures.SWAMP_OAK,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.OAK_LOG, Blocks.OAK_LEAVES, belowTrunkProvider,
                                8, 2, 0,
                                3, 1.0F, 0.5F
                        ).decorators(List.of(
                                new LeaveVineDecorator(0.05F),
                                new BigBranchTreeDecorator(BlockStateProvider.simple(Blocks.OAK_LOG), 0.5F)
                        )).build()
                )
        );

        entries.add(
                VegetationFeatures.WATERLILY,
                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                new WeightedStateProvider(WeightedList.<BlockState>builder()
                                        .add(Blocks.LILY_PAD.defaultBlockState(), 4)
                                        .add(HollowBlocks.FLOWERING_LILY_PAD.defaultBlockState(), 1)
                                        .build())
                        )
                )
        );

        entries.add(
                TreeFeatures.FALLEN_BIRCH_TREE,
                new ConfiguredFeature<>(
                        Feature.FALLEN_TREE,
                        createFallenTree(HollowBlocks.BIRCH_HOLLOW_LOG.hollowLog(), 5, 8).build()
                )
        );

        entries.add(
                TreeFeatures.FALLEN_SUPER_BIRCH_TREE,
                new ConfiguredFeature<>(
                        Feature.FALLEN_TREE,
                        createFallenTree(HollowBlocks.BIRCH_HOLLOW_LOG.hollowLog(), 5, 15).build()
                )
        );
    }

    private FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenTree(
            Block logBlock,
            int minLength, int maxLength
    ) {
        return new FallenTreeConfiguration.FallenTreeConfigurationBuilder(
                BlockStateProvider.simple(logBlock),
                UniformInt.of(minLength, maxLength)
        )
                .logDecorators(
                        ImmutableList.of(
                                new AttachedToLogsDecorator(
                                        0.5F,
                                        new WeightedStateProvider(
                                                WeightedList.<BlockState>builder()
                                                        .add(Blocks.MOSS_CARPET.defaultBlockState(), 10)
                                                        .add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
                                                        .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)
                                        ),
                                        List.of(Direction.UP)
                                )
                        )
                );
    }

    @Override
    public String getName() {
        return "Configured Features";
    }
}
