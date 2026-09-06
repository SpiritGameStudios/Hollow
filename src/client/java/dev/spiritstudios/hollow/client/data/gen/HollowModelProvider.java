package dev.spiritstudios.hollow.client.data.gen;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.client.color.item.Jeb;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.world.item.HollowItems;
import dev.spiritstudios.hollow.world.level.block.*;
import dev.spiritstudios.hollow.world.level.block.jar.BaseJarBlock;
import dev.spiritstudios.hollow.world.level.block.state.properties.LilyPadPiece;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Map;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public final class HollowModelProvider extends FabricModelProvider {
	private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
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

	private static final ItemTintSource BLANK_LAYER = ItemModelUtils.constantTint(-1);

	public HollowModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generators) {
		HollowBlocks.HOLLOW_LOG.forEach(block -> registerHollowLog(generators, block));
		HollowBlocks.STRIPPED_HOLLOW_LOG.forEach(block -> registerHollowLog(generators, block));

		registerHollowLogLayers(generators);

		registerSculkJaw(generators);

		registerStoneChest(HollowBlocks.STONE_CHEST, generators);
		registerStoneChest(HollowBlocks.STONE_CHEST_LID, generators);

		registerDoubleTallRotated(HollowBlocks.ECHOING_VASE, generators, false);
		registerDoubleTallRotated(HollowBlocks.SCREAMING_VASE, generators, true);

		generators.createNonTemplateHorizontalBlock(HollowBlocks.ECHOING_POT);
		generators.createNonTemplateHorizontalBlock(HollowBlocks.OBABO);

		registerWithRandomHorizontalRotations(generators, HollowBlocks.FLOWERING_LILY_PAD);
		registerGiantLilyPad(generators);
		registerCattail(generators);
		registerPolypore(generators);

		generators.registerSimpleItemModel(HollowItems.SWITCHGRASS, generators.createFlatItemModelWithBlockTexture(HollowItems.SWITCHGRASS, Blocks.FIREFLY_BUSH));
		generators.createCrossBlock(
			HollowBlocks.SWITCHGRASS,
			PlantType.NOT_TINTED,
			TextureMapping.cross(Blocks.FIREFLY_BUSH)
		);

		WeatheringCopper.WeatherState.forEach(state -> {
			Block unwaxed = HollowBlocks.COPPER_PILLAR.weathering().pick(state);
			Block waxed = HollowBlocks.COPPER_PILLAR.waxed().pick(state);

			MultiVariant verticalVariant = plainVariant(TexturedModel.COLUMN_ALT.create(unwaxed, generators.modelOutput));
			MultiVariant horizontalVariant = plainVariant(TexturedModel.COLUMN_HORIZONTAL_ALT.create(unwaxed, generators.modelOutput));

			generators.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(unwaxed, verticalVariant, horizontalVariant));
			generators.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(waxed, verticalVariant, horizontalVariant));

			generators.itemModelOutput.copy(unwaxed.asItem(), waxed.asItem());
		});

		generators.registerSimpleFlatItemModel(HollowItems.GLASS_JAR);

		registerGlassJar(HollowBlocks.GLASS_JAR, generators);
		registerGlassJar(HollowBlocks.FIREFLY_JAR, generators);
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators) {
		generators.generateFlatItem(HollowItems.MUSIC_DISC_POSTMORTEM, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.MUSIC_DISC_ONLY_YOU, ModelTemplates.FLAT_ITEM);

		generators.generateFlatItem(HollowItems.ACACIA_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.BAMBOO_FURNACE_RAFT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.BIRCH_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.CHERRY_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.DARK_OAK_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.JUNGLE_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.MANGROVE_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.OAK_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.PALE_OAK_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);
		generators.generateFlatItem(HollowItems.SPRUCE_FURNACE_BOAT, ModelTemplates.FLAT_ITEM);

		Identifier model = generators.generateLayeredItem(
			HollowItems.FIREFLY_JAR,
			TextureMapping.getItemTexture(HollowItems.GLASS_JAR),
			TextureMapping.getItemTexture(HollowItems.FIREFLY_JAR, "_overlay")
		);

		generators.itemModelOutput.accept(HollowItems.FIREFLY_JAR, ItemModelUtils.tintedModel(model, BLANK_LAYER, new Jeb()));
		generators.generateItemWithTintedOverlay(HollowItems.CATTAIL, ItemModelUtils.constantTint(0xFF6DAF3B));

		generators.generateBooleanDispatch(
			HollowItems.COPPER_HORN,
			ItemModelUtils.isUsingItem(),
			ItemModelUtils.plainModel(BuiltInRegistries.ITEM.getKey(HollowItems.COPPER_HORN).withPrefix("item/tooting_")),
			ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(HollowItems.COPPER_HORN))
		);

		generators.itemModelOutput.accept(
			HollowItems.FLOWERING_LILY_PAD,
			ItemModelUtils.tintedModel(
				generators.generateLayeredItem(
					HollowItems.FLOWERING_LILY_PAD,
					TextureMapping.getBlockTexture(Blocks.LILY_PAD),
					new Material(HollowBlockItemIds.FLOWERING_LILY_PAD.item().identifier().withPrefix("block/notreallyablockbutitneedstobeintheblockatlas/"))
				),
				ItemModelUtils.constantTint(BlockColors.LILY_PAD_DEFAULT), ItemModelUtils.constantTint(-1)
			)
		);

		generators.itemModelOutput.accept(
			HollowItems.OBABO,
			ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(HollowBlocks.OBABO)),
			new ClientItem.Properties(true, true, 1.0F)
		);
	}

	// region Helpers
	public static void registerGlassJar(Block block, BlockModelGenerators generators) {
		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(createBooleanModelDispatch(
				BaseJarBlock.HANGING,
				plainVariant(ModelLocationUtils.getModelLocation(HollowBlocks.GLASS_JAR, "_hanging")),
				plainVariant(ModelLocationUtils.getModelLocation(HollowBlocks.GLASS_JAR))
			))
		);
	}

	public static void registerSculkJaw(BlockModelGenerators generators) {
		MultiVariant inactive = plainVariant(ModelTemplates.CUBE_TOP.create(
			HollowBlocks.SCULK_JAW,
			new TextureMapping()
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(HollowBlocks.SCULK_JAW))
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK)),
			generators.modelOutput
		));

		MultiVariant active = plainVariant(ModelTemplates.CUBE_TOP.create(
			ModelLocationUtils.getModelLocation(HollowBlocks.SCULK_JAW, "_active"),
			new TextureMapping()
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(HollowBlocks.SCULK_JAW, "_active"))
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.SCULK)),
			generators.modelOutput
		));

		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.SCULK_JAW)
			.with(createBooleanModelDispatch(
				SculkJawBlock.ACTIVE,
				active, inactive
			)));
	}

	public static void registerDoubleTallRotated(Block block, BlockModelGenerators generators, boolean up) {
		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(VerticalDoubleBlock.HALF)
				.select(DoubleBlockHalf.LOWER, plainVariant(ModelLocationUtils.getModelLocation(block)))
				.select(DoubleBlockHalf.UPPER, plainVariant(ModelLocationUtils.getModelLocation(block, "_upper")))
			)
			.with(up ? NORTH_DEFAULT_ROTATION_OPERATIONS : ROTATION_HORIZONTAL_FACING));
	}

	public static void registerPolypore(BlockModelGenerators generators) {
		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.POLYPORE)
			.with(PropertyDispatch.initial(PolyporeBlock.POLYPORE_AMOUNT)
				.select(1, plainVariant(Hollow.id("block/one_polypore")))
				.select(2, plainVariant(Hollow.id("block/two_polypore")))
				.select(3, plainVariant(Hollow.id("block/three_polypore")))
			).with(ROTATION_HORIZONTAL_FACING));

		generators.registerSimpleFlatItemModel(HollowItems.POLYPORE);
	}

	public static void registerStoneChest(Block block, BlockModelGenerators generators) {
		Identifier id = ModelLocationUtils.getModelLocation(block);

		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(StoneChestBlock.CHEST_TYPE)
				.select(ChestType.SINGLE, plainVariant(id))
				.select(ChestType.LEFT, plainVariant(ModelLocationUtils.getModelLocation(block, "_left")))
				.select(ChestType.RIGHT, plainVariant(ModelLocationUtils.getModelLocation(block, "_right")))
			).with(ROTATION_HORIZONTAL_FACING));

		generators.registerSimpleItemModel(block.asItem(), id);
	}

	private static void registerWithRandomHorizontalRotations(BlockModelGenerators generator, Block block) {
		Variant normal = plainModel(ModelLocationUtils.getModelLocation(block));
		Variant mirrored = plainModel(ModelLocationUtils.getModelLocation(block, "_mirrored"));

		MultiVariant variants = variants(
			normal,
			mirrored,
			normal.with(Y_ROT_90),
			mirrored.with(Y_ROT_90),
			normal.with(Y_ROT_180),
			mirrored.with(Y_ROT_180),
			normal.with(Y_ROT_270),
			mirrored.with(Y_ROT_270)
		);

		generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variants));
	}

	public static void registerCattail(BlockModelGenerators generator) {
		Block block = HollowBlocks.CATTAIL;
		generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(PropertyDispatch.initial(CattailBlock.THIRD).generate(third ->
				plainVariant(ModelLocationUtils.getModelLocation(block, "_" + third.getSerializedName()))
			))
		);
	}

	private static void registerHollowLog(BlockModelGenerators generators, Block block) {
		Identifier hollowLog = HollowTexturedModels.HOLLOW_LOG.create(block, generators.modelOutput);
		Identifier hollowLogHorizontal = HollowTexturedModels.HOLLOW_LOG_HORIZONTAL.create(block, generators.modelOutput);

		generators.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(block, plainVariant(hollowLog), plainVariant(hollowLogHorizontal)));
	}

	private static void registerHollowLogLayers(BlockModelGenerators generators) {
		Identifier moss = Hollow.id("block/moss_overhang");
		Identifier paleMoss = Hollow.id("block/pale_moss_overhang");

		HollowModelTemplates.HOLLOW_LOG_LAYER.create(moss, TextureMapping.defaultTexture(new Material(moss)), generators.modelOutput);
		HollowModelTemplates.HOLLOW_LOG_LAYER.create(paleMoss, TextureMapping.defaultTexture(new Material(paleMoss)), generators.modelOutput);
	}

	private static void registerGiantLilyPad(BlockModelGenerators generators) {
		MultiVariant[] modelIds = new MultiVariant[4];
		for (int i = 0; i < 4; i++) {
			modelIds[i] = plainVariant(Hollow.id("block/giant_lily_pad_" + i));
		}

		Map<LilyPadPiece, MultiVariant> north = ImmutableMap.of(
			LilyPadPiece.NORTH_WEST, modelIds[1],
			LilyPadPiece.NORTH_EAST, modelIds[0],
			LilyPadPiece.SOUTH_EAST, modelIds[3],
			LilyPadPiece.SOUTH_WEST, modelIds[2]
		);

		Map<LilyPadPiece, MultiVariant> south = ImmutableMap.of(
			LilyPadPiece.NORTH_WEST, modelIds[3],
			LilyPadPiece.NORTH_EAST, modelIds[2],
			LilyPadPiece.SOUTH_EAST, modelIds[1],
			LilyPadPiece.SOUTH_WEST, modelIds[0]
		);

		Map<LilyPadPiece, MultiVariant> east = ImmutableMap.of(
			LilyPadPiece.NORTH_WEST, modelIds[2],
			LilyPadPiece.NORTH_EAST, modelIds[1],
			LilyPadPiece.SOUTH_EAST, modelIds[0],
			LilyPadPiece.SOUTH_WEST, modelIds[3]
		);

		Map<LilyPadPiece, MultiVariant> west = ImmutableMap.of(
			LilyPadPiece.NORTH_WEST, modelIds[0],
			LilyPadPiece.NORTH_EAST, modelIds[3],
			LilyPadPiece.SOUTH_EAST, modelIds[2],
			LilyPadPiece.SOUTH_WEST, modelIds[1]
		);

		generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(HollowBlocks.GIANT_LILY_PAD)
			.with(PropertyDispatch.initial(GiantLilyPadBlock.FACING, GiantLilyPadBlock.PIECE).generate(
				(direction, piece) -> {
					MultiVariant variant = switch (direction) {
						case NORTH -> north.get(piece);
						case SOUTH -> south.get(piece);
						case EAST -> east.get(piece);
						case WEST -> west.get(piece);
						default -> throw new IllegalStateException();
					};

					assert variant != null;
					return variant.with(facingNorthDefault(direction));
				}
			)));
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
