package dev.spiritstudios.hollow.datagen;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.hollow.block.CattailStemBlock;
import dev.spiritstudios.hollow.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.block.SculkJawBlock;
import dev.spiritstudios.hollow.block.StoneChestBlock;
import dev.spiritstudios.hollow.block.VerticalDoubleBlock;
import dev.spiritstudios.hollow.block.HollowBlocks;
import dev.spiritstudios.hollow.item.HollowItems;
import dev.spiritstudios.specter.api.core.exception.UnreachableException;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.data.BlockModelDefinitionCreator;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Map;
import java.util.Optional;

import static dev.spiritstudios.hollow.Hollow.MODID;
import static dev.spiritstudios.hollow.Hollow.id;
import static net.minecraft.client.data.BlockStateModelGenerator.NO_OP;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_90;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_90;
import static net.minecraft.client.data.BlockStateModelGenerator.createBooleanModelMap;
import static net.minecraft.client.data.BlockStateModelGenerator.createModelVariant;
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;
import static net.minecraft.client.data.BlockStateModelGenerator.modelWithYRotation;

public class HollowModelProvider extends FabricModelProvider {
	private static final BlockStateVariantMap<ModelVariantOperator> NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.HORIZONTAL_FACING)
			.register(Direction.EAST, ROTATE_Y_90)
			.register(Direction.SOUTH, ROTATE_Y_180)
			.register(Direction.WEST, ROTATE_Y_270)
			.register(Direction.NORTH, NO_OP);

	private static final BlockStateVariantMap<ModelVariantOperator> NORTH_DEFAULT_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.FACING)
			.register(Direction.DOWN, ROTATE_X_90)
			.register(Direction.UP, ROTATE_X_270)
			.register(Direction.NORTH, NO_OP)
			.register(Direction.SOUTH, ROTATE_Y_180)
			.register(Direction.WEST, ROTATE_Y_270)
			.register(Direction.EAST, ROTATE_Y_90);

	public HollowModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
		ReflectionHelper.getStaticFields(HollowBlocks.class, HollowLogBlock.class)
				.forEach(pair -> registerHollowLog(generator, pair.value()));

		generator.registerFlowerPotPlantAndItem(HollowBlocks.PAEONIA, HollowBlocks.POTTED_PAEONIA, BlockStateModelGenerator.CrossType.NOT_TINTED);

		generator.registerFlowerPotPlantAndItem(HollowBlocks.ROOTED_ORCHID, HollowBlocks.POTTED_ROOTED_ORCHID, BlockStateModelGenerator.CrossType.NOT_TINTED);

		generator.registerDoubleBlock(HollowBlocks.CAMPION, BlockStateModelGenerator.CrossType.NOT_TINTED);
		generator.registerItemModel(HollowBlocks.CAMPION.asItem());


		registerWithRandomHorizontalRotations(generator, HollowBlocks.TWIG);
		registerWithRandomHorizontalRotations(generator, HollowBlocks.LOTUS_LILYPAD);

		generator.registerNorthDefaultHorizontalRotation(HollowBlocks.ECHOING_POT);
		registerDoubleTallRotated(HollowBlocks.ECHOING_VASE, generator, false);
		registerDoubleTallRotated(HollowBlocks.SCREAMING_VASE, generator, true);

		registerSculkJaw(generator);

		registerStoneChest(HollowBlocks.STONE_CHEST, generator);
		generator.registerItemModel(
				HollowBlocks.STONE_CHEST.asItem(),
				ModelIds.getBlockModelId(HollowBlocks.STONE_CHEST)
		);

		registerStoneChest(HollowBlocks.STONE_CHEST_LID, generator);
		generator.registerItemModel(
				HollowBlocks.STONE_CHEST_LID.asItem(),
				ModelIds.getBlockModelId(HollowBlocks.STONE_CHEST_LID)
		);


		registerGiantLilypad(generator);
		registerCattailStem(generator);

		registerPolypore(generator);
		generator.registerItemModel(HollowBlocks.POLYPORE.asItem());

		generator.registerTintableCross(
				HollowBlocks.CATTAIL,
				BlockStateModelGenerator.CrossType.NOT_TINTED,
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

//        generator.registerSpawnEgg(
//                HollowItems.FIREFLY_SPAWN_EGG,
//                0x102F4E, 0xCAAF94
//        );

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
		WeightedVariant inactive = createWeightedVariant(Models.CUBE_TOP.upload(
				HollowBlocks.SCULK_JAW,
				new TextureMap()
						.put(TextureKey.TOP, TextureMap.getId(HollowBlocks.SCULK_JAW))
						.put(TextureKey.SIDE, TextureMap.getId(Blocks.SCULK)),
				generator.modelCollector
		));

		WeightedVariant active = createWeightedVariant(Models.CUBE_TOP.upload(
				ModelIds.getBlockSubModelId(HollowBlocks.SCULK_JAW, "_active"),
				new TextureMap()
						.put(TextureKey.TOP, TextureMap.getSubId(HollowBlocks.SCULK_JAW, "_active"))
						.put(TextureKey.SIDE, TextureMap.getId(Blocks.SCULK)),
				generator.modelCollector
		));

		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(HollowBlocks.SCULK_JAW)
				.with(createBooleanModelMap(
						SculkJawBlock.ACTIVE,
						active, inactive
				)));
	}

	public final void registerDoubleTallRotated(Block block, BlockStateModelGenerator generator, boolean up) {
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block)
				.with(BlockStateVariantMap.models(VerticalDoubleBlock.HALF)
						.register(DoubleBlockHalf.LOWER, createWeightedVariant(ModelIds.getBlockModelId(block)))
						.register(DoubleBlockHalf.UPPER, createWeightedVariant(ModelIds.getBlockSubModelId(block, "_upper")))
				)
				.coordinate(up ? NORTH_DEFAULT_ROTATION_OPERATIONS : NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
	}

	public final void registerPolypore(BlockStateModelGenerator generator) {
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(HollowBlocks.POLYPORE)
				.with(BlockStateVariantMap.models(PolyporeBlock.POLYPORE_AMOUNT)
						.register(
								1,
								createWeightedVariant(id("block/one_polypore"))
						)
						.register(
								2,
								createWeightedVariant(id("block/two_polypore"))
						)
						.register(
								3,
								createWeightedVariant(id("block/three_polypore"))
						)
				).coordinate(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
	}

	public final void registerStoneChest(Block block, BlockStateModelGenerator generator) {
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block)
				.with(BlockStateVariantMap.models(StoneChestBlock.CHEST_TYPE)
						.register(
								ChestType.SINGLE,
								createWeightedVariant(ModelIds.getBlockModelId(block))
						)
						.register(
								ChestType.LEFT,
								createWeightedVariant(ModelIds.getBlockSubModelId(block, "_left"))
						)
						.register(
								ChestType.RIGHT,
								createWeightedVariant(ModelIds.getBlockSubModelId(block, "_right"))
						)
				).coordinate(NORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS));
	}

	private void registerWithRandomHorizontalRotations(BlockStateModelGenerator generator, Block block) {
		ModelVariant modelVariant = createModelVariant(ModelIds.getBlockModelId(block));
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, modelWithYRotation(modelVariant)));
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

		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(HollowBlocks.CATTAIL_STEM)
				.with(createBooleanModelMap(
						CattailStemBlock.BOTTOM,
						createWeightedVariant(bottom),
						createWeightedVariant(createModelVariant(middle), createModelVariant(middle2))
				)));
	}

	private static void registerHollowLog(BlockStateModelGenerator generator, HollowLogBlock block) {
		Identifier hollowLog = HollowTexturedModels.HOLLOW_LOG.upload(block, generator.modelCollector);
		Identifier hollowLogHorizontal = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL.upload(block, generator.modelCollector);
		Identifier hollowLogHorizontalMoss = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_MOSS.upload(block, generator.modelCollector);
		Identifier hollowLogHorizontalPaleMoss = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_PALE_MOSS.upload(block, generator.modelCollector);
		Identifier hollowLogHorizontalSnow = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL_SNOW.upload(block, generator.modelCollector);

		generator.blockStateCollector.accept(createAxisRotatedBlockStateWithLayer(block, hollowLog, hollowLogHorizontal, hollowLogHorizontalMoss, hollowLogHorizontalPaleMoss, hollowLogHorizontalSnow));
	}

	private static void registerCopperPillarWaxed(BlockStateModelGenerator blockStateModelGenerator, Block block, Block unWaxed) {
		blockStateModelGenerator.registerAxisRotated(
				block,
				TexturedModel.makeFactory(b -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN),
				TexturedModel.makeFactory(b -> TextureMap.sideAndEndForTop(unWaxed), Models.CUBE_COLUMN_HORIZONTAL)
		);
	}

	private static void registerGiantLilypad(BlockStateModelGenerator generator) {
		WeightedVariant[] modelIds = new WeightedVariant[4];
		for (int i = 0; i < 4; i++) {
			TextureMap textureMap = TextureMap.texture(TextureMap.getSubId(
					HollowBlocks.GIANT_LILYPAD, "_" + i
			));
			Model model = new Model(
					Optional.of(Identifier.of(MODID, "block/giant_lilypad_template")),
					Optional.of("_" + i),
					TextureKey.TEXTURE
			);

			modelIds[i] = createWeightedVariant(model.upload(HollowBlocks.GIANT_LILYPAD, textureMap, generator.modelCollector));
		}

		Map<GiantLilyPadBlock.Piece, WeightedVariant> north = ImmutableMap.of(
				GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[1],
				GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[0],
				GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[3],
				GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[2]
		);

		Map<GiantLilyPadBlock.Piece, WeightedVariant> south = ImmutableMap.of(
				GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[3],
				GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[2],
				GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[1],
				GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[0]
		);

		Map<GiantLilyPadBlock.Piece, WeightedVariant> east = ImmutableMap.of(
				GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[2],
				GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[1],
				GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[0],
				GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[3]
		);

		Map<GiantLilyPadBlock.Piece, WeightedVariant> west = ImmutableMap.of(
				GiantLilyPadBlock.Piece.NORTH_WEST, modelIds[0],
				GiantLilyPadBlock.Piece.NORTH_EAST, modelIds[3],
				GiantLilyPadBlock.Piece.SOUTH_EAST, modelIds[2],
				GiantLilyPadBlock.Piece.SOUTH_WEST, modelIds[1]
		);

		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(HollowBlocks.GIANT_LILYPAD)
				.with(BlockStateVariantMap.models(GiantLilyPadBlock.FACING, GiantLilyPadBlock.PIECE).generate(
						(direction, piece) -> (switch (direction) {
							case DOWN, UP -> throw new UnreachableException();
							case NORTH -> north.get(piece);
							case SOUTH -> south.get(piece);
							case EAST -> east.get(piece);
							case WEST -> west.get(piece);
						}).apply(facingNorthDefault(direction))
				)));
	}

	private static BlockModelDefinitionCreator createAxisRotatedBlockStateWithLayer(Block block, Identifier verticalModelId, Identifier horizontalModelId, Identifier horizontalMossModelId, Identifier horizontalPaleMossModelId, Identifier horizontalSnowModelId) {
		return VariantsBlockModelDefinitionCreator.of(block)
				.with(BlockStateVariantMap.models(Properties.AXIS, HollowLogBlock.LAYER)
						.generate((axis, layer) -> switch (axis) {
							case Y -> createWeightedVariant(verticalModelId);
							case X, Z -> createWeightedVariant(switch (layer) {
								case NONE -> horizontalModelId;
								case MOSS -> horizontalMossModelId;
								case PALE_MOSS -> horizontalPaleMossModelId;
								case SNOW -> horizontalSnowModelId;
							}).apply(axisNorthDefault(axis));
						}));
	}

	private static ModelVariantOperator axisNorthDefault(Direction.Axis axis) {
		return switch (axis) {
			case Y -> NO_OP;
			case Z -> ROTATE_X_90;
			case X -> ROTATE_X_90.then(ROTATE_Y_90);
		};
	}

	private static ModelVariantOperator facingNorthDefault(Direction direction) {
		return switch (direction) {
			case Direction.DOWN -> ROTATE_X_90;
			case Direction.UP -> ROTATE_X_270;
			case Direction.NORTH -> NO_OP;
			case Direction.SOUTH -> ROTATE_Y_180;
			case Direction.WEST -> ROTATE_Y_270;
			case Direction.EAST -> ROTATE_Y_90;
		};
	}
	// endregion
}
