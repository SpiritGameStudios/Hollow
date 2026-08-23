package dev.spiritstudios.hollow.client.data.gen;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.world.level.block.CattailStemBlock;
import dev.spiritstudios.hollow.world.level.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.world.level.block.HollowLogBlock;
import dev.spiritstudios.hollow.world.level.block.PolyporeBlock;
import dev.spiritstudios.hollow.world.level.block.SculkJawBlock;
import dev.spiritstudios.hollow.world.level.block.StoneChestBlock;
import dev.spiritstudios.hollow.world.level.block.VerticalDoubleBlock;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.item.HollowItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;

import java.util.Map;
import java.util.function.Consumer;

import static dev.spiritstudios.hollow.Hollow.id;
import static net.minecraft.client.data.models.BlockModelGenerators.*;

public final class HollowModelProvider extends FabricModelProvider {
    private static final PropertyDispatch<VariantMutator> NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, Y_ROT_90)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270)
            .select(Direction.NORTH, NOP);

    private static final PropertyDispatch<VariantMutator> NORTH_DEFAULT_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.FACING)
            .select(Direction.DOWN, X_ROT_90)
            .select(Direction.UP, X_ROT_270)
            .select(Direction.NORTH, NOP)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270)
            .select(Direction.EAST, Y_ROT_90);

    public HollowModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        Consumer<Block> hollowLogGen = block -> {
            if (!(block instanceof HollowLogBlock log)) throw new IllegalStateException();
            registerHollowLog(generator, log);
        };

        HollowBlocks.OAK_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.SPRUCE_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.BIRCH_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.JUNGLE_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.ACACIA_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.CHERRY_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.PALE_OAK_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.DARK_OAK_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.MANGROVE_HOLLOW_LOG.forEach(hollowLogGen);
        HollowBlocks.CRIMSON_HOLLOW_STEM.forEach(hollowLogGen);
        HollowBlocks.WARPED_HOLLOW_STEM.forEach(hollowLogGen);

        registerWithRandomHorizontalRotations(generator, HollowBlocks.FLOWERING_LILY_PAD);

        generator.createNonTemplateHorizontalBlock(HollowBlocks.ECHOING_POT);
        registerDoubleTallRotated(HollowBlocks.ECHOING_VASE, generator, false);
        registerDoubleTallRotated(HollowBlocks.SCREAMING_VASE, generator, true);

        registerSculkJaw(generator);

        registerStoneChest(HollowBlocks.STONE_CHEST, generator);
        generator.registerSimpleItemModel(
                HollowBlocks.STONE_CHEST.asItem(),
                ModelLocationUtils.getModelLocation(HollowBlocks.STONE_CHEST)
        );

        registerStoneChest(HollowBlocks.STONE_CHEST_LID, generator);
        generator.registerSimpleItemModel(
                HollowBlocks.STONE_CHEST_LID.asItem(),
                ModelLocationUtils.getModelLocation(HollowBlocks.STONE_CHEST_LID)
        );


        registerGiantLilyPad(generator);
        registerCattailStem(generator);

        registerPolypore(generator);
        generator.registerSimpleFlatItemModel(HollowBlocks.POLYPORE.asItem());

		generator.registerSimpleItemModel(HollowItems.SWITCHGRASS, generator.createFlatItemModelWithBlockTexture(HollowItems.SWITCHGRASS, Blocks.FIREFLY_BUSH));
		generator.createCrossBlock(
				HollowBlocks.SWITCHGRASS,
				PlantType.NOT_TINTED,
				TextureMapping.cross(Blocks.FIREFLY_BUSH)
		);

		generator.createCrossBlockWithDefaultItem(
                HollowBlocks.CATTAIL,
                BlockModelGenerators.PlantType.NOT_TINTED,
                TextureMapping.cross(HollowBlocks.CATTAIL)
        );

        HollowBlocks.COPPER_PILLAR.zipUnwaxedWaxed((unwaxed, _) -> generator.createRotatedPillarWithHorizontalVariant(unwaxed, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT));
        HollowBlocks.COPPER_PILLAR.zipUnwaxedWaxed(generator::copyModel);

        ;

        generator.registerSimpleFlatItemModel(HollowBlocks.JAR.asItem());
        generator.createNonTemplateModelBlock(HollowBlocks.JAR);

        generator.registerSimpleFlatItemModel(HollowBlocks.JAR_OF_FIREFLIES.asItem());
        generator.createNonTemplateModelBlock(HollowBlocks.JAR_OF_FIREFLIES, HollowBlocks.JAR);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(HollowItems.MUSIC_DISC_POSTMORTEM, ModelTemplates.FLAT_ITEM);

//        generator.registerSpawnEgg(
//                HollowItems.FIREFLY_SPAWN_EGG,
//                0x102F4E, 0xCAAF94
//        );

        generator.generateBooleanDispatch(
                HollowItems.COPPER_HORN,
                ItemModelUtils.isUsingItem(),
                ItemModelUtils.plainModel(BuiltInRegistries.ITEM.getKey(HollowItems.COPPER_HORN).withPrefix("item/tooting_")),
                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(HollowItems.COPPER_HORN))
        );

        generator.itemModelOutput.accept(
                HollowItems.GIANT_LILY_PAD,
                ItemModelUtils.tintedModel(
                        generator.createFlatItemModel(
                                HollowItems.GIANT_LILY_PAD,
                                ModelTemplates.FLAT_ITEM
                        ),
                        ItemModelUtils.constantTint(BlockColors.LILY_PAD_DEFAULT), ItemModelUtils.constantTint(-1)
                )
        );


        generator.itemModelOutput.accept(
                HollowItems.FLOWERING_LILY_PAD,
                ItemModelUtils.tintedModel(
                        generator.generateLayeredItem(
                                HollowItems.FLOWERING_LILY_PAD,
                                TextureMapping.getBlockTexture(Blocks.LILY_PAD),
                                new Material(HollowBlockItemIds.FLOWERING_LILY_PAD.item().identifier().withPrefix("block/notreallyablockbutitneedstobeintheblockatlas/"))
                        ),
                        ItemModelUtils.constantTint(BlockColors.LILY_PAD_DEFAULT), ItemModelUtils.constantTint(-1)
                )
        );
    }

    // region Helpers
    public final void registerSculkJaw(BlockModelGenerators generator) {
        MultiVariant inactive = plainVariant(ModelTemplates.CUBE_TOP.create(
                HollowBlocks.SCULK_JAW,
                new TextureMapping()
                        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(HollowBlocks.SCULK_JAW))
                        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK)),
                generator.modelOutput
        ));

        MultiVariant active = plainVariant(ModelTemplates.CUBE_TOP.create(
                ModelLocationUtils.getModelLocation(HollowBlocks.SCULK_JAW, "_active"),
                new TextureMapping()
                        .put(TextureSlot.TOP, TextureMapping.getBlockTexture(HollowBlocks.SCULK_JAW, "_active"))
                        .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK)),
                generator.modelOutput
        ));

        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.SCULK_JAW)
                .with(createBooleanModelDispatch(
                        SculkJawBlock.ACTIVE,
                        active, inactive
                )));
    }

    public final void registerDoubleTallRotated(Block block, BlockModelGenerators generator, boolean up) {
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(VerticalDoubleBlock.HALF)
                        .select(DoubleBlockHalf.LOWER, plainVariant(ModelLocationUtils.getModelLocation(block)))
                        .select(DoubleBlockHalf.UPPER, plainVariant(ModelLocationUtils.getModelLocation(block, "_upper")))
                )
                .with(up ? NORTH_DEFAULT_ROTATION_OPERATIONS : NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
    }

    public final void registerPolypore(BlockModelGenerators generator) {
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.POLYPORE)
                .with(PropertyDispatch.initial(PolyporeBlock.POLYPORE_AMOUNT)
                        .select(
                                1,
                                plainVariant(id("block/one_polypore"))
                        )
                        .select(
                                2,
                                plainVariant(id("block/two_polypore"))
                        )
                        .select(
                                3,
                                plainVariant(id("block/three_polypore"))
                        )
                ).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
    }

    public final void registerStoneChest(Block block, BlockModelGenerators generator) {
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(StoneChestBlock.CHEST_TYPE)
                        .select(
                                ChestType.SINGLE,
                                plainVariant(ModelLocationUtils.getModelLocation(block))
                        )
                        .select(
                                ChestType.LEFT,
                                plainVariant(ModelLocationUtils.getModelLocation(block, "_left"))
                        )
                        .select(
                                ChestType.RIGHT,
                                plainVariant(ModelLocationUtils.getModelLocation(block, "_right"))
                        )
                ).with(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
    }

    private void registerWithRandomHorizontalRotations(BlockModelGenerators generator, Block block) {
        Variant modelVariant = plainModel(ModelLocationUtils.getModelLocation(block));
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, createRotatedVariants(modelVariant)));
    }

    public final void registerCattailStem(BlockModelGenerators generator) {
        Identifier middle = ModelTemplates.CROSS.create(
                HollowBlocks.CATTAIL_STEM,
                TextureMapping.cross(HollowBlocks.CATTAIL_STEM),
                generator.modelOutput
        );

        Identifier middle2 = ModelTemplates.CROSS.create(
                ModelLocationUtils.getModelLocation(HollowBlocks.CATTAIL_STEM, "_2"),
                TextureMapping.cross(TextureMapping.getBlockTexture(HollowBlocks.CATTAIL_STEM, "_2")),
                generator.modelOutput
        );

        Identifier bottom = ModelTemplates.CROSS.create(
                ModelLocationUtils.getModelLocation(HollowBlocks.CATTAIL_STEM, "_bottom"),
                TextureMapping.cross(TextureMapping.getBlockTexture(HollowBlocks.CATTAIL_STEM, "_bottom")),
                generator.modelOutput
        );

        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.CATTAIL_STEM)
                .with(createBooleanModelDispatch(
                        CattailStemBlock.BOTTOM,
                        plainVariant(bottom),
                        variants(plainModel(middle), plainModel(middle2))
                )));
    }

    private static void registerHollowLog(BlockModelGenerators generator, HollowLogBlock block) {
        Identifier hollowLog = HollowTexturedModels.HOLLOW_LOG.create(block, generator.modelOutput);
        Identifier hollowLogHorizontal = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL.create(block, generator.modelOutput);
        Identifier hollowLogHorizontalMoss = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_MOSS.create(block, generator.modelOutput);
        Identifier hollowLogHorizontalPaleMoss = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_PALE_MOSS.create(block, generator.modelOutput);
        Identifier hollowLogHorizontalSnow = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_SNOW.create(block, generator.modelOutput);

        generator.blockStateOutput.accept(createAxisRotatedBlockStateWithLayer(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMoss, hollowLogHorizontalPaleMoss, hollowLogHorizontalSnow));
    }

    private static void registerGiantLilyPad(BlockModelGenerators generator) {
        MultiVariant[] modelIds = new MultiVariant[4];
        for (int i = 0; i < 4; i++) {
            modelIds[i] = plainVariant(id("block/giant_lily_pad_" + i));
        }

        Map<GiantLilyPadBlock.Piece, MultiVariant> north = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[1],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[0],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[3],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[2]
        );

        Map<GiantLilyPadBlock.Piece, MultiVariant> south = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[3],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[2],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[1],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[0]
        );

        Map<GiantLilyPadBlock.Piece, MultiVariant> east = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[2],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[1],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[0],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[3]
        );

        Map<GiantLilyPadBlock.Piece, MultiVariant> west = ImmutableMap.of(
                GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[0],
                GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[3],
                GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[2],
                GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[1]
        );

        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.GIANT_LILY_PAD)
                .with(PropertyDispatch.initial(GiantLilyPadBlock.FACING, GiantLilyPadBlock.PIECE).generate(
                        (direction, piece) -> (switch (direction) {
                            case DOWN, UP -> throw new IllegalStateException();
                            case NORTH -> north.get(piece);
                            case SOUTH -> south.get(piece);
                            case EAST -> east.get(piece);
                            case WEST -> west.get(piece);
                        }).with(facingNorthDefault(direction))
                )));
    }

    private static BlockModelDefinitionGenerator createAxisRotatedBlockStateWithLayer(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossModelId, Identifier horizontalPaleMossModelId, Identifier horizontalSnowModelId) {
        return MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.AXIS, HollowLogBlock.LAYER)
                        .generate((axis, layer) -> switch (axis) {
                            case Y -> plainVariant(verticalModelId);
                            case X, Z -> plainVariant(switch (layer) {
                                case NONE -> horizontalModelId;
                                case MOSS -> horizontalMossModelId;
                                case PALE_MOSS -> horizontalPaleMossModelId;
                                case SNOW -> horizontalSnowModelId;
                            }).with(axisNorthDefault(axis));
                        }));
    }

    private static VariantMutator axisNorthDefault(Direction.Axis axis) {
        return switch (axis) {
            case Y -> NOP;
            case Z -> X_ROT_90;
            case X -> X_ROT_90.then(Y_ROT_90);
        };
    }

    private static VariantMutator facingNorthDefault(Direction direction) {
        return switch (direction) {
            case Direction.DOWN -> X_ROT_90;
            case Direction.UP -> X_ROT_270;
            case Direction.NORTH -> NOP;
            case Direction.SOUTH -> Y_ROT_180;
            case Direction.WEST -> Y_ROT_270;
            case Direction.EAST -> Y_ROT_90;
        };
    }
    // endregion
}
