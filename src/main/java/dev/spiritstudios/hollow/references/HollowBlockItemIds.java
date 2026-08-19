package dev.spiritstudios.hollow.references;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.world.level.block.HollowLogCollection;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.WeatheringCopperCollection;

public final class HollowBlockItemIds {
    public static final BlockItemId FLOWERING_LILY_PAD = create("flowering_lily_pad");
    public static final BlockItemId GIANT_LILY_PAD = create("giant_lily_pad");

    public static final BlockItemId CATTAIL = create("cattail");
    public static final BlockItemId POLYPORE = create("polypore");

    public static final HollowLogCollection<BlockItemId> OAK_HOLLOW_LOG = createHollowLog("oak");
    public static final HollowLogCollection<BlockItemId> SPRUCE_HOLLOW_LOG = createHollowLog("spruce");
    public static final HollowLogCollection<BlockItemId> BIRCH_HOLLOW_LOG = createHollowLog("birch");
    public static final HollowLogCollection<BlockItemId> JUNGLE_HOLLOW_LOG = createHollowLog("jungle");
    public static final HollowLogCollection<BlockItemId> ACACIA_HOLLOW_LOG = createHollowLog("acacia");
    public static final HollowLogCollection<BlockItemId> CHERRY_HOLLOW_LOG = createHollowLog("cherry");
    public static final HollowLogCollection<BlockItemId> PALE_OAK_HOLLOW_LOG = createHollowLog("pale_oak");
    public static final HollowLogCollection<BlockItemId> DARK_OAK_HOLLOW_LOG = createHollowLog("dark_oak");
    public static final HollowLogCollection<BlockItemId> MANGROVE_HOLLOW_LOG = createHollowLog("mangrove");
    public static final HollowLogCollection<BlockItemId> CRIMSON_HOLLOW_STEM = createHollowStem("crimson");
    public static final HollowLogCollection<BlockItemId> WARPED_HOLLOW_STEM = createHollowStem("warped");

    public static final BlockItemId JAR = create("jar");
    public static final BlockItemId JAR_OF_FIREFLIES = create("jar_of_fireflies");

    public static final BlockItemId ECHOING_POT = create("echoing_pot");
    public static final BlockItemId ECHOING_VASE = create("echoing_vase");
    public static final BlockItemId SCREAMING_VASE = create("screaming_vase");
    public static final BlockItemId OBABO = create("obabo");

    public static final BlockItemId STONE_CHEST = create("stone_chest");
    public static final BlockItemId STONE_CHEST_LID = create("stone_chest_lid");

    public static final BlockItemId SCULK_JAW = create("sculk_jaw");

    public static final WeatheringCopperCollection<BlockItemId> COPPER_PILLAR = createSimpleCopper("copper_pillar");

	public static final BlockItemId SWITCHGRASS = create("switchgrass");

    private static HollowLogCollection<BlockItemId> createHollowLog(final String baseName) {
        return HollowLogCollection.log(baseName).map(HollowBlockItemIds::create);
    }

    private static HollowLogCollection<BlockItemId> createHollowStem(final String baseName) {
        return HollowLogCollection.stem(baseName).map(HollowBlockItemIds::create);
    }

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
