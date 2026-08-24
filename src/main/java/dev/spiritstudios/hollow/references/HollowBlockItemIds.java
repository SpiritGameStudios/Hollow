package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.LogCollection;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class HollowBlockItemIds {
	public static final BlockItemId FLOWERING_LILY_PAD = create("flowering_lily_pad");
	public static final BlockItemId CATTAIL = create("cattail");
	public static final BlockItemId POLYPORE = create("polypore");

	public static final LogCollection<BlockItemId> HOLLOW_LOG = new LogCollection<>(
		create("hollow_oak_log"),
		create("hollow_spruce_log"),
		create("hollow_birch_log"),
		create("hollow_jungle_log"),
		create("hollow_acacia_log"),
		create("hollow_dark_oak_log"),
		create("hollow_mangrove_log"),
		create("hollow_cherry_log"),
		create("hollow_pale_oak_log"),
		create("hollow_crimson_stem"),
		create("hollow_warped_stem")
	);

	public static final LogCollection<BlockItemId> STRIPPED_HOLLOW_LOG = new LogCollection<>(
		create("stripped_hollow_oak_log"),
		create("stripped_hollow_spruce_log"),
		create("stripped_hollow_birch_log"),
		create("stripped_hollow_jungle_log"),
		create("stripped_hollow_acacia_log"),
		create("stripped_hollow_dark_oak_log"),
		create("stripped_hollow_mangrove_log"),
		create("stripped_hollow_cherry_log"),
		create("stripped_hollow_pale_oak_log"),
		create("stripped_hollow_crimson_stem"),
		create("stripped_hollow_warped_stem")
	);

	public static final BlockItemId GLASS_JAR = create("glass_jar");
	public static final BlockItemId FIREFLY_JAR = create("firefly_jar");

	public static final BlockItemId ECHOING_POT = create("echoing_pot");
	public static final BlockItemId ECHOING_VASE = create("echoing_vase");
	public static final BlockItemId SCREAMING_VASE = create("screaming_vase");
	public static final BlockItemId OBABO = create("obabo");

	public static final BlockItemId STONE_CHEST = create("stone_chest");
	public static final BlockItemId STONE_CHEST_LID = create("stone_chest_lid");

	public static final BlockItemId SCULK_JAW = create("sculk_jaw");

	public static final WeatheringCopperCollection<BlockItemId> COPPER_PILLAR = createSimpleCopper("copper_pillar");

	public static final BlockItemId SWITCHGRASS = create("switchgrass");

	private static ColorCollection<BlockItemId> createSimpleColored(final String baseName) {
		return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(HollowBlockItemIds::create);
	}

	private static WeatheringCopperCollection<BlockItemId> createSimpleCopper(final String baseName) {
		return WeatheringCopperCollection.prefixWithState(WeatheringCopperCollection.create(baseName)).map(HollowBlockItemIds::create);
	}

	private static BlockItemId create(final String blockName, final String itemName) {
		return BlockItemId.create(Hollow.id(blockName), Hollow.id(itemName));
	}

	private static BlockItemId create(final String name) {
		Identifier id = Hollow.id(name);
		return BlockItemId.create(id, id);
	}
}
