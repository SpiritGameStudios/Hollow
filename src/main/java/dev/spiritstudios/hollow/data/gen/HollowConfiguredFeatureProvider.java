package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.BigBranchTreeDecorator;
import dev.spiritstudios.hollow.world.level.gen.tree.decorator.BranchTreeDecorator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BlockStateProviders;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.spiritstudios.hollow.world.level.gen.feature.HollowConfiguredFeatures.hangingLeavestreeBuilder;

public class HollowConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public HollowConfiguredFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        HolderLookup<Feature> lookup = registries.lookupOrThrow(Registries.FEATURE);
        HolderLookup<BlockStateProvider> biomes = registries.lookupOrThrow(Registries.BLOCK_STATE_PROVIDER);

        lookup.listElementIds()
                .filter(key ->
                        key.identifier().getNamespace().equals(Hollow.MODID))
                .forEach(key ->
                        entries.add(key, lookup.getOrThrow(key).value()));

        Holder<BlockStateProvider> belowTrunkProvider = biomes.getOrThrow(BlockStateProviders.SOIL_BENEATH_TREE);

        entries.add(
                TreeFeatures.BIRCH_BEES_0002,
                hangingLeavestreeBuilder(
						Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, belowTrunkProvider,
						8, 5, 0,
						2, 0.25F, 0.4F
				).decorators(List.of(
						new ShelfMushroomDecorator(0.4F),
						new BranchTreeDecorator(BlockStateProvider.holderOf(Blocks.BIRCH_LOG), 0.5F, 2)
				)).ignoreVines().build()
        );

        entries.add(
                TreeFeatures.SUPER_BIRCH_BEES_0002,
                hangingLeavestreeBuilder(
						Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, belowTrunkProvider,
						8, 5, 6,
						2, 0.25F, 0.4F
				).decorators(List.of(
						new ShelfMushroomDecorator(0.4F),
						new BranchTreeDecorator(BlockStateProvider.holderOf(Blocks.BIRCH_LOG), 0.5F, 5)
				)).ignoreVines().build()
        );

        entries.add(
                TreeFeatures.SWAMP_OAK,
                hangingLeavestreeBuilder(
						Blocks.OAK_LOG, Blocks.OAK_LEAVES, belowTrunkProvider,
						8, 2, 0,
						3, 1.0F, 0.5F
				).decorators(List.of(
						new LeaveVineDecorator(0.05F),
						new BigBranchTreeDecorator(BlockStateProvider.holderOf(Blocks.OAK_LOG), 0.5F)
				)).build()
        );

		WeightedList.Builder<BlockState> waterlilyStates = WeightedList.<BlockState>builder()
			.add(Blocks.LILY_PAD.defaultBlockState(), 910)
			.add(HollowBlocks.FLOWERING_LILY_PAD.defaultBlockState(), 50);

		List<Direction> giantDirections = GiantLilyPadBlock.FACING.getPossibleValues();

		for (Direction value : giantDirections) {
			waterlilyStates.add(
				HollowBlocks.GIANT_LILY_PAD.defaultBlockState().setValue(GiantLilyPadBlock.FACING, value),
				40 / giantDirections.size()
			);
		}

		entries.add(
			VegetationFeatures.WATERLILY,
			new SimpleBlockFeature(new WeightedStateProvider(waterlilyStates.build()))
        );


		entries.add(
			TreeFeatures.FALLEN_OAK_TREE,
			createFallenTree(HollowBlocks.HOLLOW_LOG.oak(), 4, 7)
				.stumpDecorator(TrunkVineDecorator.INSTANCE)
				.logDecorator(vanillaDecorator())
				.build()
		);

        entries.add(
			TreeFeatures.FALLEN_BIRCH_TREE,
			createFallenTree(HollowBlocks.HOLLOW_LOG.birch(), 5, 8)
				.logDecorator(mossAndMushroomsDecorator())
				.build()
        );

        entries.add(
			TreeFeatures.FALLEN_SUPER_BIRCH_TREE,
			createFallenTree(HollowBlocks.HOLLOW_LOG.birch(), 5, 15)
				.logDecorator(mossAndMushroomsDecorator())
				.build()
        );

		entries.add(
			TreeFeatures.FALLEN_SPRUCE_TREE,
			createFallenTree(HollowBlocks.HOLLOW_LOG.spruce(), 6, 10)
				.logDecorator(vanillaDecorator())
				.build()
		);

		entries.add(
			TreeFeatures.FALLEN_JUNGLE_TREE,
			createFallenTree(HollowBlocks.HOLLOW_LOG.jungle(), 4, 11)
				.stumpDecorator(TrunkVineDecorator.INSTANCE)
				.logDecorator(mossAndMushroomsDecorator())
				.build()
		);
    }

    private FallenTreeFeature.Builder createFallenTree(Block logBlock, int minLength, int maxLength) {
        return FallenTreeFeature.builder(BlockStateProvider.of(logBlock), UniformInt.of(minLength, maxLength));
    }

	private TreeDecorator mossAndMushroomsDecorator() {
		return new AttachedToLogsDecorator(
			0.5F,
			Holder.direct(new WeightedStateProvider(
				WeightedList.<BlockState>builder()
					.add(Blocks.MOSS_CARPET.defaultBlockState(), 10)
					.add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
					.add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)
			)),
			List.of(Direction.UP)
		);
	}

	private TreeDecorator vanillaDecorator() {
		return new AttachedToLogsDecorator(
			0.1F,
			Holder.direct(new WeightedStateProvider(
				WeightedList.<BlockState>builder()
					.add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
					.add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)
			)),
			List.of(Direction.UP)
		);
	}

    @Override
    public String getName() {
        return "Configured Features";
    }
}
