package dev.spiritstudios.hollow.datagen;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.SculkJawBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateSupplier;
import net.minecraft.client.data.BlockStateVariant;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.MultipartBlockStateSupplier;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantSettings;
import net.minecraft.client.data.VariantsBlockStateSupplier;
import net.minecraft.client.data.When;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

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

        generator.registerFlowerPotPlant(HollowBlocks.PAEONIA, HollowBlocks.POTTED_PAEONIA, BlockStateModelGenerator.CrossType.NOT_TINTED);
        generator.registerItemModel(HollowBlocks.PAEONIA);

        generator.registerFlowerPotPlant(HollowBlocks.ROOTED_ORCHID, HollowBlocks.POTTED_ROOTED_ORCHID, BlockStateModelGenerator.CrossType.NOT_TINTED);
        generator.registerItemModel(HollowBlocks.ROOTED_ORCHID);

        Identifier campionTop = generator.createSubModel(HollowBlocks.CAMPION, "_top", BlockStateModelGenerator.CrossType.NOT_TINTED.getCrossModel(), TextureMap::cross);
        Identifier campionBottom = generator.createSubModel(HollowBlocks.CAMPION, "_bottom", BlockStateModelGenerator.CrossType.NOT_TINTED.getCrossModel(), TextureMap::cross);

        generator.registerDoubleBlock(HollowBlocks.CAMPION, campionTop, campionBottom);
        generator.registerItemModel(HollowBlocks.CAMPION.asItem());

        generator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlocks.TWIG, ModelIds.getBlockModelId(HollowBlocks.TWIG)));
        generator.registerItemModel(HollowBlocks.TWIG);

        generator.blockStateCollector.accept(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(HollowBlocks.LOTUS_LILYPAD, ModelIds.getBlockModelId(HollowBlocks.LOTUS_LILYPAD)));

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(HollowBlocks.ECHOING_POT, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(HollowBlocks.ECHOING_POT)))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));

        registerSculkJaw(generator);

        createGiantLilyPadBlockState(generator);

        generator.registerItemModel(HollowBlocks.CATTAIL, "_top");
        generator.registerTintableCrossBlockState(
                HollowBlocks.CATTAIL,
                BlockStateModelGenerator.CrossType.NOT_TINTED,
                TextureMap.cross(TextureMap.getSubId(HollowBlocks.CATTAIL, "_top"))
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

//        registerFlowerbed(generator, HollowBlocks.PINK_WILDFLOWER);
//        registerFlowerbed(generator, HollowBlocks.BLUE_WILDFLOWER);
//        registerFlowerbed(generator, HollowBlocks.WHITE_WILDFLOWER);
//        registerFlowerbed(generator, HollowBlocks.PURPLE_WILDFLOWER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(HollowBlocks.POLYPORE.asItem(), Models.GENERATED);

        generator.register(HollowItems.MUSIC_DISC_POSTMORTEM, Models.GENERATED);

        generator.registerSpawnEgg(
                HollowItems.FIREFLY_SPAWN_EGG,
                0x102F4E, 0xCAAF94
        );

        generator.registerCondition(
                HollowItems.COPPER_HORN,
                ItemModels.usingItemProperty(),
                ItemModels.basic(Registries.ITEM.getId(HollowItems.COPPER_HORN).withPrefixedPath("item/tooting_")),
                ItemModels.basic(ModelIds.getItemModelId(HollowItems.COPPER_HORN))
        );

        generator.output.accept(
                HollowItems.GIANT_LILYPAD,
                ItemModels.tinted(
                        generator.upload(
                                HollowItems.GIANT_LILYPAD,
                                Models.GENERATED
                        ),
                        ItemModels.constantTintSource(0x71C35C), ItemModels.constantTintSource(-1)
                )
        );

        generator.output.accept(
                HollowItems.LOTUS_LILYPAD,
                ItemModels.tinted(
                        generator.uploadTwoLayers(
                                HollowItems.LOTUS_LILYPAD,
                                TextureMap.getId(Blocks.LILY_PAD),
                                TextureMap.getId(HollowItems.LOTUS_LILYPAD)
                        ),
                        ItemModels.constantTintSource(0x71C35C), ItemModels.constantTintSource(-1)
                )
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

    private static void createGiantLilyPadBlockState(BlockStateModelGenerator blockStateModelGenerator) {
        Identifier[] modelIds = new Identifier[4];
        for (int i = 0; i < 4; i++) {
            TextureMap textureMap = new TextureMap().put(TextureKey.TEXTURE, Identifier.of(MODID, "block/giant_lilypad_" + i));
            Model model = new Model(Optional.of(Identifier.of(MODID, "block/giant_lilypad_template")), Optional.of("_" + i), TextureKey.TEXTURE);
            modelIds[i] = model.upload(HollowBlocks.GIANT_LILYPAD, textureMap, blockStateModelGenerator.modelCollector);
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

        BlockStateSupplier supplier = VariantsBlockStateSupplier.create(HollowBlocks.GIANT_LILYPAD).coordinate(
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
    }

    private static BlockStateSupplier createAxisRotatedBlockStateWithLayer(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossModelId, Identifier horizontalPaleMossModelId, Identifier horizontalSnowModelId) {
        return VariantsBlockStateSupplier.create(block)
                .coordinate(
                        BlockStateVariantMap.create(Properties.AXIS, HollowLogBlock.LAYER)
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.NONE, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalModelId)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.NONE,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalMossModelId)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.MOSS,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalMossModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.PALE_MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.PALE_MOSS, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalPaleMossModelId)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.PALE_MOSS,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalPaleMossModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                                .register(Direction.Axis.Y, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, verticalModelId))
                                .register(Direction.Axis.Z, HollowLogBlock.Layer.SNOW, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, horizontalSnowModelId)
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(
                                        Direction.Axis.X,
                                        HollowLogBlock.Layer.SNOW,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, horizontalSnowModelId)
                                                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                )
                );
    }

    public static final TexturedModel.Factory FLOWERBED_1 = TexturedModel.makeFactory(
            block -> new TextureMap()
                    .put(TextureKey.FLOWERBED, TextureMap.getId(block))
                    .put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem")),
            Models.FLOWERBED_1
    );
    public static final TexturedModel.Factory FLOWERBED_2 = TexturedModel.makeFactory(
            block -> new TextureMap()
                    .put(TextureKey.FLOWERBED, TextureMap.getId(block))
                    .put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem")),
            Models.FLOWERBED_2
    );
    public static final TexturedModel.Factory FLOWERBED_3 = TexturedModel.makeFactory(
            block -> new TextureMap()
                    .put(TextureKey.FLOWERBED, TextureMap.getId(block))
                    .put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem")),
            Models.FLOWERBED_3
    );
    public static final TexturedModel.Factory FLOWERBED_4 = TexturedModel.makeFactory(
            block -> new TextureMap()
                    .put(TextureKey.FLOWERBED, TextureMap.getId(block))
                    .put(TextureKey.STEM, Identifier.ofVanilla("block/pink_petals_stem")),
            Models.FLOWERBED_4
    );

    private static void registerFlowerbed(BlockStateModelGenerator generator, Block flowerbed) {
        generator.registerItemModel(flowerbed.asItem());
        Identifier one = FLOWERBED_1.upload(flowerbed, generator.modelCollector);
        Identifier two = FLOWERBED_2.upload(flowerbed, generator.modelCollector);
        Identifier three = FLOWERBED_3.upload(flowerbed, generator.modelCollector);
        Identifier four = FLOWERBED_4.upload(flowerbed, generator.modelCollector);

        generator.blockStateCollector.accept(
                MultipartBlockStateSupplier.create(flowerbed)
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, one)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, one).put(VariantSettings.Y, VariantSettings.Rotation.R90)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, one).put(VariantSettings.Y, VariantSettings.Rotation.R180)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 1, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, one).put(VariantSettings.Y, VariantSettings.Rotation.R270)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, two)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, two).put(VariantSettings.Y, VariantSettings.Rotation.R90)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, two).put(VariantSettings.Y, VariantSettings.Rotation.R180)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 2, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, two).put(VariantSettings.Y, VariantSettings.Rotation.R270)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, three)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, three).put(VariantSettings.Y, VariantSettings.Rotation.R90)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, three).put(VariantSettings.Y, VariantSettings.Rotation.R180)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 3, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, three).put(VariantSettings.Y, VariantSettings.Rotation.R270)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, four)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, four).put(VariantSettings.Y, VariantSettings.Rotation.R90)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                BlockStateVariant.create().put(VariantSettings.MODEL, four).put(VariantSettings.Y, VariantSettings.Rotation.R180)
                        )
                        .with(
                                When.create().set(Properties.FLOWER_AMOUNT, 4).set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                BlockStateVariant.create().put(VariantSettings.MODEL, four).put(VariantSettings.Y, VariantSettings.Rotation.R270)
                        )
        );
    }

    // endregion
}
