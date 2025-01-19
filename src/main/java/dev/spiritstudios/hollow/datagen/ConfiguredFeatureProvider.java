package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.worldgen.decorator.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.BranchTreeDecorator;
import dev.spiritstudios.hollow.worldgen.decorator.PolyporeTreeDecorator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DataPool;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.spiritstudios.hollow.worldgen.feature.HollowConfiguredFeatures.createRandomPatch;
import static dev.spiritstudios.hollow.worldgen.feature.HollowConfiguredFeatures.hangingLeavestreeBuilder;

public class ConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public ConfiguredFeatureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<ConfiguredFeature<?, ?>> lookup = registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE);

        lookup.streamKeys()
                .filter(key ->
                        key.getValue().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));

        entries.add(
                TreeConfiguredFeatures.BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES,
                                8, 5, 0,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                new PolyporeTreeDecorator(BlockStateProvider.of(HollowBlocks.POLYPORE)),
                                new BranchTreeDecorator(BlockStateProvider.of(Blocks.BIRCH_LOG), 0.5F, 2)
                        )).ignoreVines().build()
                )
        );

        entries.add(
                TreeConfiguredFeatures.SUPER_BIRCH_BEES_0002,
                new ConfiguredFeature<>(
                        Feature.TREE,
                        hangingLeavestreeBuilder(
                                Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES,
                                8, 5, 6,
                                2, 0.25F, 0.4F
                        ).decorators(List.of(
                                new PolyporeTreeDecorator(BlockStateProvider.of(HollowBlocks.POLYPORE)),
                                new BranchTreeDecorator(BlockStateProvider.of(Blocks.BIRCH_LOG), 0.5F, 5)
                        )).ignoreVines().build()
                )
        );

        entries.add(
                TreeConfiguredFeatures.SWAMP_OAK,
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

        entries.add(
                VegetationConfiguredFeatures.PATCH_WATERLILY,
                createRandomPatch(
                        new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                                .add(Blocks.LILY_PAD.getDefaultState(), 4)
                                .add(HollowBlocks.LOTUS_LILYPAD.getDefaultState(), 1)
                                .build()),
                        10
                )
        );

        entries.add(
                VegetationConfiguredFeatures.FLOWER_SWAMP,
                new ConfiguredFeature<>(
                        Feature.FLOWER,
                        new RandomPatchFeatureConfig(
                                64, 6, 2,
                                PlacedFeatures.createEntry(
                                        Feature.SIMPLE_BLOCK,
                                        new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder()
                                                .add(Blocks.BLUE_ORCHID.getDefaultState())
                                                .add(HollowBlocks.ROOTED_ORCHID.getDefaultState())
                                                .build()))
                                )
                        )
                )
        );
    }

    @Override
    public String getName() {
        return "Hollow/Configured Features";
    }
}
