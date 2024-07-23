package dev.callmeecho.hollow.datagen;

import com.google.common.collect.ImmutableMap;
import dev.callmeecho.cabinetapi.util.ReflectionHelper;
import dev.callmeecho.hollow.main.block.GiantLilyPadBlock;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Map;
import java.util.Optional;

import static dev.callmeecho.hollow.main.Hollow.MODID;

public class ModelProvider extends FabricModelProvider {
    public static final Model HOLLOW_LOG = new Model(Optional.of(Identifier.of(MODID, "block/" + "hollow_log_template")), Optional.empty(), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    public static final Model HOLLOW_LOG_HORIZONTAL = new Model(Optional.of(Identifier.of(MODID, "block/" + "hollow_log_horizontal_template")), Optional.of("_horizontal"), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);
    public static final Model HOLLOW_LOG_HORIZONTAL_MOSSY = new Model(Optional.of(Identifier.of(MODID, "block/" + "hollow_log_horizontal_mossy_template")), Optional.of("_horizontal_mossy"), TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END);

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    private static void createGiantLilyPadBlockState(BlockStateModelGenerator blockStateModelGenerator) {
        Identifier[] modelIds = new Identifier[4];
        for (int i = 0; i < 4; i++) {
            TextureMap textureMap = new TextureMap().put(TextureKey.TEXTURE, Identifier.of(MODID, "block/giant_lilypad_" + i));
            Model model = new Model(Optional.of(Identifier.of(MODID, "block/giant_lilypad_template")), Optional.of("_" + i), TextureKey.TEXTURE);
            modelIds[i] = model.upload(HollowBlockRegistry.GIANT_LILYPAD, textureMap, blockStateModelGenerator.modelCollector);
        }

        Map<GiantLilyPadBlock.Piece, Identifier> north = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[1],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[0],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[3],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[2]
        );

        Map<GiantLilyPadBlock.Piece, Identifier> south = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[3],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[2],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[1],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[0]
        );

        Map<GiantLilyPadBlock.Piece, Identifier> east = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[2],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[1],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[0],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[3]
        );

        Map<GiantLilyPadBlock.Piece, Identifier> west = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[0],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[3],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[2],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[1]
        );

        BlockStateSupplier supplier = VariantsBlockStateSupplier.create(HollowBlockRegistry.GIANT_LILYPAD).coordinate(
                BlockStateVariantMap.create(GiantLilyPadBlock.FACING, GiantLilyPadBlock.PIECE).register(
                        (direction, piece) -> {
                            BlockStateVariant variant = BlockStateVariant.create();
                            switch (direction) {
                                case NORTH -> variant.put(VariantSettings.MODEL, north.get(piece));
                                case SOUTH -> {
                                    variant.put(VariantSettings.MODEL, south.get(piece));
                                    variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
                                }

                                case EAST -> {
                                    variant.put(VariantSettings.MODEL, east.get(piece));
                                    variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
                                }

                                case WEST -> {
                                    variant.put(VariantSettings.MODEL, west.get(piece));
                                    variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
                                }
                            }

                            return variant;
                        }
                )
        );

        blockStateModelGenerator.blockStateCollector.accept(supplier);
        blockStateModelGenerator.excludeFromSimpleItemModelGeneration(HollowBlockRegistry.GIANT_LILYPAD);
    }

    private static BlockStateSupplier createAxisRotatedBlockStateWithMossy(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossyModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.AXIS, HollowLogBlock.MOSSY)
                                .register(Direction.Axis.Y, false, BlockStateVariant.create().put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, false, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalModelId).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        false,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, true, BlockStateVariant.create().put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, true, BlockStateVariant.create().put(VariantSettings.MODEL, horizontalMossyModelId).put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        true,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalMossyModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                );
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ReflectionHelper.forEachStaticField(HollowBlockRegistry.class, HollowLogBlock.class, (block, name, field) -> {
            TextureMap textureMap = new TextureMap().put(TextureKey.SIDE, Identifier.of("minecraft", "block/" + block.sideTexture)).put(TextureKey.INSIDE, Identifier.ofVanilla("block/" + block.insideTexture)).put(TextureKey.END, Identifier.of("minecraft", "block/" + block.endTexture));
            Identifier hollowLog = HOLLOW_LOG.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            Identifier hollowLogHorizontal = HOLLOW_LOG_HORIZONTAL.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            Identifier hollowLogHorizontalMossy = HOLLOW_LOG_HORIZONTAL_MOSSY.upload(block, textureMap, blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(createAxisRotatedBlockStateWithMossy(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMossy));
        });

        blockStateModelGenerator.registerFlowerPotPlant(HollowBlockRegistry.PAEONIA, HollowBlockRegistry.POTTED_PAEONIA, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerFlowerPotPlant(HollowBlockRegistry.ROOTED_ORCHID, HollowBlockRegistry.POTTED_ROOTED_ORCHID, BlockStateModelGenerator.TintType.NOT_TINTED);

        Identifier campionTop = blockStateModelGenerator.createSubModel(HollowBlockRegistry.CAMPION, "_top", BlockStateModelGenerator.TintType.NOT_TINTED.getCrossModel(), TextureMap::cross);
        Identifier campionBottom = blockStateModelGenerator.createSubModel(HollowBlockRegistry.CAMPION, "_bottom", BlockStateModelGenerator.TintType.NOT_TINTED.getCrossModel(), TextureMap::cross);
        blockStateModelGenerator.registerDoubleBlock(HollowBlockRegistry.CAMPION, campionTop, campionBottom);
        blockStateModelGenerator.excludeFromSimpleItemModelGeneration(HollowBlockRegistry.CAMPION);

        blockStateModelGenerator.registerItemModel(HollowBlockRegistry.TWIG);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlockRegistry.TWIG, ModelIds.getBlockModelId(HollowBlockRegistry.TWIG)));

        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlockRegistry.LOTUS_LILYPAD, ModelIds.getBlockModelId(HollowBlockRegistry.LOTUS_LILYPAD)));
        blockStateModelGenerator.excludeFromSimpleItemModelGeneration(HollowBlockRegistry.LOTUS_LILYPAD);

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(HollowBlockRegistry.ECHOING_POT, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(HollowBlockRegistry.ECHOING_POT)))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));

        createGiantLilyPadBlockState(blockStateModelGenerator);

        blockStateModelGenerator.registerAxisRotated(HollowBlockRegistry.COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        blockStateModelGenerator.registerAxisRotated(HollowBlockRegistry.EXPOSED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        blockStateModelGenerator.registerAxisRotated(HollowBlockRegistry.WEATHERED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        blockStateModelGenerator.registerAxisRotated(HollowBlockRegistry.OXIDIZED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);

        registerCopperPillarWaxed(blockStateModelGenerator, HollowBlockRegistry.WAXED_COPPER_PILLAR, HollowBlockRegistry.COPPER_PILLAR);
        registerCopperPillarWaxed(blockStateModelGenerator, HollowBlockRegistry.WAXED_EXPOSED_COPPER_PILLAR, HollowBlockRegistry.EXPOSED_COPPER_PILLAR);
        registerCopperPillarWaxed(blockStateModelGenerator, HollowBlockRegistry.WAXED_WEATHERED_COPPER_PILLAR, HollowBlockRegistry.WEATHERED_COPPER_PILLAR);
        registerCopperPillarWaxed(blockStateModelGenerator, HollowBlockRegistry.WAXED_OXIDIZED_COPPER_PILLAR, HollowBlockRegistry.OXIDIZED_COPPER_PILLAR);

        blockStateModelGenerator.excludeFromSimpleItemModelGeneration(HollowBlockRegistry.JAR);
        blockStateModelGenerator.excludeFromSimpleItemModelGeneration(HollowBlockRegistry.FIREFLY_JAR);
        blockStateModelGenerator.registerSimpleState(HollowBlockRegistry.JAR);
        blockStateModelGenerator.registerStateWithModelReference(HollowBlockRegistry.FIREFLY_JAR, HollowBlockRegistry.JAR);
    }

    public void registerCopperPillarWaxed(BlockStateModelGenerator blockStateModelGenerator, Block block, Block unWaxed) {
        blockStateModelGenerator.registerAxisRotated(
                block,
                TexturedModel.makeFactory((Block b) -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN),
                TexturedModel.makeFactory((Block b) -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN_HORIZONTAL)
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
