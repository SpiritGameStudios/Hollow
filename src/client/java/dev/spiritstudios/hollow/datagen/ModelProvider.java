package dev.spiritstudios.hollow.datagen;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.CattailStemBlock;
import dev.spiritstudios.hollow.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.block.SculkJawBlock;
import dev.spiritstudios.hollow.block.StoneChestBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.ChestType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.BlockStateVariant;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.SimpleModelSupplier;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dev.spiritstudios.hollow.Hollow.MODID;

public class ModelProvider extends FabricModelProvider {
    public static final TexturedModel.Factory HOLLOW_LOG = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture());
            },
            new Model(
                    Optional.of(Identifier.of(MODID, "block/hollow_log_template")),
                    Optional.empty(),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture());
            },
            new Model(
                    Optional.of(Identifier.of(MODID, "block/hollow_log_horizontal_template")),
                    Optional.of("_horizontal"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_MOSS = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/moss_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_moss"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_PALE_MOSS = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/pale_moss_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_pale_moss"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public static final TexturedModel.Factory HOLLOW_LOG_HORIZONTAL_SNOW = TexturedModel.makeFactory(
            b -> {
                if (!(b instanceof HollowLogBlock block))
                    throw new IllegalArgumentException();

                return new TextureMap()
                        .put(TextureKey.SIDE, block.typeData.sideTexture())
                        .put(TextureKey.INSIDE, block.typeData.insideTexture())
                        .put(TextureKey.END, block.typeData.endTexture())
                        .put(TextureKey.LAYER0, Hollow.id("block/snow_overlay"));
            },
            new Model(
                    Optional.of(Hollow.id("block/hollow_log_horizontal_layer_template")),
                    Optional.of("_horizontal_snow"),
                    TextureKey.SIDE, TextureKey.INSIDE, TextureKey.END, TextureKey.LAYER0
            )
    );

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        ReflectionHelper.getStaticFields(HollowBlocks.class, HollowLogBlock.class)
                .forEach(pair -> registerHollowLog(generator, pair.value()));

        generator.registerFlowerPotPlant(HollowBlocks.PAEONIA, HollowBlocks.POTTED_PAEONIA, BlockStateModelGenerator.TintType.NOT_TINTED);

        generator.registerFlowerPotPlant(HollowBlocks.ROOTED_ORCHID, HollowBlocks.POTTED_ROOTED_ORCHID, BlockStateModelGenerator.TintType.NOT_TINTED);

        generator.registerDoubleBlock(
                HollowBlocks.CAMPION,
                generator.createSubModel(HollowBlocks.CAMPION, "_top", BlockStateModelGenerator.TintType.NOT_TINTED.getCrossModel(), TextureMap::cross),
                generator.createSubModel(HollowBlocks.CAMPION, "_bottom",  BlockStateModelGenerator.TintType.NOT_TINTED.getCrossModel(), TextureMap::cross)
        );
        generator.registerItemModel(HollowBlocks.CAMPION.asItem());

        generator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlocks.TWIG, ModelIds.getBlockModelId(HollowBlocks.TWIG)));
        generator.registerItemModel(HollowBlocks.TWIG);

        generator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlocks.LOTUS_LILYPAD, ModelIds.getBlockModelId(HollowBlocks.LOTUS_LILYPAD)));
        Models.GENERATED_TWO_LAYERS.upload(
                ModelIds.getItemModelId(HollowItems.LOTUS_LILYPAD),
                TextureMap.layered(
                        TextureMap.getId(Blocks.LILY_PAD),
                        TextureMap.getId(HollowItems.LOTUS_LILYPAD)
                ),
                generator.modelCollector
        );

        generator.registerNorthDefaultHorizontalRotation(HollowBlocks.ECHOING_POT);
        generator.registerNorthDefaultHorizontalRotation(HollowBlocks.ECHOING_VASE);

        registerSculkJaw(generator);

        registerStoneChest(HollowBlocks.STONE_CHEST, generator);
        registerStoneChest(HollowBlocks.STONE_CHEST_LID, generator);

        registerGiantLilypad(generator);
        generator.registerItemModel(HollowItems.GIANT_LILYPAD);

        registerCattailStem(generator);

        registerPolypore(generator);
        generator.registerItemModel(HollowBlocks.POLYPORE.asItem());

        generator.registerTintableCross(
                HollowBlocks.CATTAIL,
                BlockStateModelGenerator.TintType.NOT_TINTED,
                TextureMap.cross(HollowBlocks.CATTAIL)
        );

        generator.registerAxisRotated(HollowBlocks.COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerAxisRotated(HollowBlocks.EXPOSED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerAxisRotated(HollowBlocks.WEATHERED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerAxisRotated(HollowBlocks.OXIDIZED_COPPER_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);

        registerCopperPillarWaxed(generator, HollowBlocks.WAXED_COPPER_PILLAR, HollowBlocks.COPPER_PILLAR);
        registerCopperPillarWaxed(generator, HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR, HollowBlocks.EXPOSED_COPPER_PILLAR);
        registerCopperPillarWaxed(generator, HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR, HollowBlocks.WEATHERED_COPPER_PILLAR);
        registerCopperPillarWaxed(generator, HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR, HollowBlocks.OXIDIZED_COPPER_PILLAR);

        generator.registerItemModel(HollowBlocks.JAR.asItem());
        generator.registerSimpleState(HollowBlocks.JAR);

        generator.registerItemModel(HollowBlocks.FIREFLY_JAR.asItem());
        generator.registerStateWithModelReference(HollowBlocks.FIREFLY_JAR, HollowBlocks.JAR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(HollowItems.MUSIC_DISC_POSTMORTEM, Models.GENERATED);

        generator.writer.accept(
                ModelIds.getItemModelId(HollowItems.FIREFLY_SPAWN_EGG),
                new SimpleModelSupplier(ModelIds.getMinecraftNamespacedItem("template_spawn_egg"))
        );
    }

    // region Helpers
    public final void registerSculkJaw(BlockStateModelGenerator generator) {
        Identifier inactive = Models.CUBE_TOP.upload(
                HollowBlocks.SCULK_JAW,
                new TextureMap()
                        .put(TextureKey.TOP, TextureMap.getId(HollowBlocks.SCULK_JAW))
                        .put(TextureKey.SIDE, TextureMap.getId(Blocks.SCULK)),
                generator.modelCollector
        );

        Identifier active = Models.CUBE_TOP.upload(
                ModelIds.getBlockSubModelId(HollowBlocks.SCULK_JAW, "_active"),
                new TextureMap()
                        .put(TextureKey.TOP, TextureMap.getSubId(HollowBlocks.SCULK_JAW, "_active"))
                        .put(TextureKey.SIDE, TextureMap.getId(Blocks.SCULK)),
                generator.modelCollector
        );

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HollowBlocks.SCULK_JAW)
                .coordinate(BlockStateModelGenerator.createBooleanModelMap(
                        SculkJawBlock.ACTIVE,
                        active, inactive
                )));
    }

    public final void registerPolypore(BlockStateModelGenerator generator) {
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HollowBlocks.POLYPORE)
                .coordinate(BlockStateVariantMap.create(PolyporeBlock.POLYPORE_AMOUNT)
                        .register(
                                1,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Hollow.id("block/one_polypore"))
                        )
                        .register(
                                2,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Hollow.id("block/two_polypore"))
                        )
                        .register(
                                3,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Hollow.id("block/three_polypore"))
                        )
                )
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    public final void registerStoneChest(Block block, BlockStateModelGenerator generator) {
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(StoneChestBlock.CHEST_TYPE)
                        .register(
                                ChestType.SINGLE,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, ModelIds.getBlockModelId(block))
                        )
                        .register(
                                ChestType.LEFT,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_left"))
                        )
                        .register(
                                ChestType.RIGHT,
                                BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(block, "_right"))
                        )
                )
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    public final void registerCattailStem(BlockStateModelGenerator generator) {
        Identifier middle = Models.CROSS.upload(
                HollowBlocks.CATTAIL_STEM,
                TextureMap.cross(HollowBlocks.CATTAIL_STEM),
                generator.modelCollector
        );

        Identifier middle2 = Models.CROSS.upload(
                ModelIds.getBlockSubModelId(HollowBlocks.CATTAIL_STEM, "_2"),
                TextureMap.cross(TextureMap.getSubId(HollowBlocks.CATTAIL_STEM, "_2")),
                generator.modelCollector
        );

        Identifier bottom = Models.CROSS.upload(
                ModelIds.getBlockSubModelId(HollowBlocks.CATTAIL_STEM, "_bottom"),
                TextureMap.cross(TextureMap.getSubId(HollowBlocks.CATTAIL_STEM, "_bottom")),
                generator.modelCollector
        );

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HollowBlocks.CATTAIL_STEM)
                .coordinate(BlockStateVariantMap.create(CattailStemBlock.BOTTOM)
                        .register(true, BlockStateVariant.create().put(VariantSettings.MODEL, bottom))
                        .register(
                                false,
                                List.of(
                                        BlockStateVariant.create().put(VariantSettings.MODEL, middle),
                                        BlockStateVariant.create().put(VariantSettings.MODEL, middle2)
                                )
                        )));
    }

    private static void registerHollowLog(BlockStateModelGenerator generator, HollowLogBlock block) {
        Identifier hollowLog = HOLLOW_LOG.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontal = HOLLOW_LOG_HORIZONTAL.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontalMoss = HOLLOW_LOG_HORIZONTAL_MOSS.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontalPaleMoss = HOLLOW_LOG_HORIZONTAL_PALE_MOSS.upload(block, generator.modelCollector);
        Identifier hollowLogHorizontalSnow = HOLLOW_LOG_HORIZONTAL_SNOW.upload(block, generator.modelCollector);

        generator.blockStateCollector.accept(createAxisRotatedBlockStateWithLayer(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMoss, hollowLogHorizontalPaleMoss, hollowLogHorizontalSnow));
    }

    private static void registerCopperPillarWaxed(BlockStateModelGenerator blockStateModelGenerator, Block block, Block unWaxed) {
        blockStateModelGenerator.registerAxisRotated(
                block,
                TexturedModel.makeFactory((Block b) -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN),
                TexturedModel.makeFactory((Block b) -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN_HORIZONTAL)
        );
    }

    private static void registerGiantLilypad(BlockStateModelGenerator generator) {
        Identifier[] modelIds = new Identifier[4];
        for (int i = 0; i < 4; i++) {
            TextureMap textureMap = TextureMap.texture(TextureMap.getSubId(
                    HollowBlocks.GIANT_LILYPAD, "_" + i
            ));
            Model model = new Model(
                    Optional.of(Identifier.of(MODID, "block/giant_lilypad_template")),
                    Optional.of("_" + i),
                    TextureKey.TEXTURE
            );

            modelIds[i] = model.upload(HollowBlocks.GIANT_LILYPAD, textureMap, generator.modelCollector);
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

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(HollowBlocks.GIANT_LILYPAD)
                .coordinate(BlockStateVariantMap.create(GiantLilyPadBlock.FACING, GiantLilyPadBlock.PIECE).register(
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
                )));
    }

    private static BlockStateSupplier createAxisRotatedBlockStateWithLayer(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossModelId, Identifier horizontalPaleMossModelId, Identifier horizontalSnowModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.AXIS, HollowLogBlock.LAYER)
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.NONE,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalMossModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.MOSS,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalMossModelId)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalSnowModelId))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.SNOW,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.MODEL, horizontalSnowModelId)
                                )
                );
    }
    // endregion
}
