package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.CampionBlock;
import dev.spiritstudios.hollow.block.CattailBlock;
import dev.spiritstudios.hollow.block.CattailStemBlock;
import dev.spiritstudios.hollow.block.EchoingPotBlock;
import dev.spiritstudios.hollow.block.EchoingVaseBlock;
import dev.spiritstudios.hollow.block.FireflyJarBlock;
import dev.spiritstudios.hollow.block.GiantLilyPadBlock;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.block.JarBlock;
import dev.spiritstudios.hollow.block.OxidizablePillarBlock;
import dev.spiritstudios.hollow.block.PolyporeBlock;
import dev.spiritstudios.hollow.block.ScreamingVaseBlock;
import dev.spiritstudios.hollow.block.SculkJawBlock;
import dev.spiritstudios.hollow.block.StoneChestBlock;
import dev.spiritstudios.hollow.block.StoneChestLidBlock;
import dev.spiritstudios.hollow.block.TwigBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class HollowBlocks {
    // region Hollow logs
    public static final Block OAK_HOLLOW_LOG = registerHollowLog("oak_hollow_log", Blocks.OAK_LOG);

    public static final Block STRIPPED_OAK_HOLLOW_LOG = registerStrippedHollowLog("stripped_oak_hollow_log", Blocks.STRIPPED_OAK_LOG);

    public static final Block SPRUCE_HOLLOW_LOG = registerHollowLog("spruce_hollow_log", Blocks.SPRUCE_LOG);
    public static final Block STRIPPED_SPRUCE_HOLLOW_LOG = registerStrippedHollowLog("stripped_spruce_hollow_log", Blocks.STRIPPED_SPRUCE_LOG);

    public static final Block BIRCH_HOLLOW_LOG = registerHollowLog("birch_hollow_log", Blocks.BIRCH_LOG);
    public static final Block STRIPPED_BIRCH_HOLLOW_LOG = registerStrippedHollowLog("stripped_birch_hollow_log", Blocks.STRIPPED_BIRCH_LOG);

    public static final Block JUNGLE_HOLLOW_LOG = registerHollowLog("jungle_hollow_log", Blocks.JUNGLE_LOG);
    public static final Block STRIPPED_JUNGLE_HOLLOW_LOG = registerStrippedHollowLog("stripped_jungle_hollow_log", Blocks.STRIPPED_JUNGLE_LOG);

    public static final Block ACACIA_HOLLOW_LOG = registerHollowLog("acacia_hollow_log", Blocks.ACACIA_LOG);
    public static final Block STRIPPED_ACACIA_HOLLOW_LOG = registerStrippedHollowLog("stripped_acacia_hollow_log", Blocks.STRIPPED_ACACIA_LOG);

    public static final Block DARK_OAK_HOLLOW_LOG = registerHollowLog("dark_oak_hollow_log", Blocks.DARK_OAK_LOG);
    public static final Block STRIPPED_DARK_OAK_HOLLOW_LOG = registerStrippedHollowLog("stripped_dark_oak_hollow_log", Blocks.STRIPPED_DARK_OAK_LOG);

    public static final Block CRIMSON_HOLLOW_STEM = registerHollowLog("crimson_hollow_stem", Blocks.CRIMSON_STEM);
    public static final Block STRIPPED_CRIMSON_HOLLOW_STEM = registerStrippedHollowLog("stripped_crimson_hollow_stem", Blocks.STRIPPED_CRIMSON_STEM);

    public static final Block WARPED_HOLLOW_STEM = registerHollowLog("warped_hollow_stem", Blocks.WARPED_STEM);
    public static final Block STRIPPED_WARPED_HOLLOW_STEM = registerStrippedHollowLog("stripped_warped_hollow_stem", Blocks.STRIPPED_WARPED_STEM);

    public static final Block MANGROVE_HOLLOW_LOG = registerHollowLog("mangrove_hollow_log", Blocks.MANGROVE_LOG);
    public static final Block STRIPPED_MANGROVE_HOLLOW_LOG = registerStrippedHollowLog("stripped_mangrove_hollow_log", Blocks.STRIPPED_MANGROVE_LOG);

    public static final Block CHERRY_HOLLOW_LOG = registerHollowLog("cherry_hollow_log", Blocks.CHERRY_LOG);
    public static final Block STRIPPED_CHERRY_HOLLOW_LOG = registerStrippedHollowLog("stripped_cherry_hollow_log", Blocks.STRIPPED_CHERRY_LOG);
    // endregion

    public static final Block ECHOING_POT = register(
            "echoing_pot",
            EchoingPotBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DECORATED_POT)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );

    public static final Block ECHOING_VASE = register(
            "echoing_vase",
            EchoingVaseBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DECORATED_POT)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );

    public static final Block OBABO = register(
            "obabo",
            EchoingVaseBlock.ObaboBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DECORATED_POT)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );

    public static final Block SCREAMING_VASE = register(
            "screaming_vase",
            ScreamingVaseBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DECORATED_POT)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );

    public static final Block PAEONIA = register(
            "paeonia",
            settings -> new FlowerBlock(
                    StatusEffects.GLOWING,
                    5,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.ALLIUM)
    );

    public static final Block ROOTED_ORCHID = register(
            "rooted_orchid",
            settings -> new FlowerBlock(
                    StatusEffects.SATURATION,
                    0.35F,
                    settings
            ),
            AbstractBlock.Settings.copy(Blocks.BLUE_ORCHID)
    );

    public static final Block CAMPION = register(
            "campion",
            CampionBlock::new,
            AbstractBlock.Settings.copy(Blocks.PEONY)
    );

    public static final Block TWIG = register(
            "twig",
            TwigBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block CATTAIL_STEM = register(
            "cattail_stem",
            CattailStemBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY),
            false
    );

    public static final CattailBlock CATTAIL = register(
            "cattail",
            CattailBlock::new,
            AbstractBlock.Settings.copy(CATTAIL_STEM)
    );

    public static final Block LOTUS_LILYPAD = register(
            "lotus_lilypad",
            LilyPadBlock::new,
            AbstractBlock.Settings.copy(Blocks.LILY_PAD),
            false
    );

    public static final Block GIANT_LILYPAD = register(
            "giant_lilypad",
            GiantLilyPadBlock::new,
            AbstractBlock.Settings.copy(Blocks.LILY_PAD),
            false
    );

    public static final Block SUPER_GIANT_LILYPAD = register(
            "super_giant_lilypad",
            GiantLilyPadBlock.SuperGiantLilyPadBlock::new,
            AbstractBlock.Settings.copy(Blocks.LILY_PAD),
            false
    );

    public static final Block POLYPORE = register(
            "polypore",
            PolyporeBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
    );

    public static final Block SCULK_JAW = register(
            "sculk_jaw",
            SculkJawBlock::new,
            AbstractBlock.Settings.copy(Blocks.SCULK)
                    .velocityMultiplier(0.25F)
                    .jumpVelocityMultiplier(0.0F)
                    .strength(3.0F, 3.0F)
                    .luminance(state -> state.get(SculkJawBlock.ACTIVE) ? 6 : 0)
    );

    public static final Block JAR = register(
            "jar",
            JarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block FIREFLY_JAR = register(
            "firefly_jar",
            FireflyJarBlock::new,
            AbstractBlock.Settings.copy(JAR).luminance(state -> 15)
    );

    public static final Block STONE_CHEST = register(
            "stone_chest",
            StoneChestBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(6.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final Block STONE_CHEST_LID = register(
            "stone_chest_lid",
            StoneChestLidBlock::new,
            AbstractBlock.Settings.copy(STONE_CHEST).strength(3.0F, 6.0F)
    );

    // region Copper Pillar
    public static final Block COPPER_PILLAR = register(
            "copper_pillar",
            settings -> new OxidizablePillarBlock(Oxidizable.OxidationLevel.UNAFFECTED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
                    .strength(3.0F, 6.0F)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.COPPER)
    );

    public static final Block EXPOSED_COPPER_PILLAR = register(
            "exposed_copper_pillar",
            settings -> new OxidizablePillarBlock(Oxidizable.OxidationLevel.EXPOSED, settings),
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );

    public static final Block WEATHERED_COPPER_PILLAR = register(
            "weathered_copper_pillar",
            settings -> new OxidizablePillarBlock(Oxidizable.OxidationLevel.WEATHERED, settings),
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.DARK_AQUA)
    );

    public static final Block OXIDIZED_COPPER_PILLAR = register(
            "oxidized_copper_pillar",
            settings -> new OxidizablePillarBlock(Oxidizable.OxidationLevel.OXIDIZED, settings),
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.TEAL)
    );

    public static final Block WAXED_COPPER_PILLAR = register(
            "waxed_copper_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.copy(COPPER_PILLAR)
    );
    public static final Block WAXED_EXPOSED_COPPER_PILLAR = register(
            "waxed_exposed_copper_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.copy(EXPOSED_COPPER_PILLAR)
    );
    public static final Block WAXED_WEATHERED_COPPER_PILLAR = register(
            "waxed_weathered_copper_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.copy(WEATHERED_COPPER_PILLAR)
    );

    public static final Block WAXED_OXIDIZED_COPPER_PILLAR = register(
            "waxed_oxidized_copper_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_PILLAR)
    );
    // endregion

    public static final Block POTTED_PAEONIA = register(
            "potted_paeonia",
            settings -> new FlowerPotBlock(PAEONIA, settings),
            AbstractBlock.Settings.create().breakInstantly().nonOpaque().pistonBehavior(PistonBehavior.DESTROY),
            false
    );

    public static final Block POTTED_ROOTED_ORCHID = register(
            "potted_rooted_orchid",
            settings -> new FlowerPotBlock(ROOTED_ORCHID, settings),
            AbstractBlock.Settings.create().breakInstantly().nonOpaque().pistonBehavior(PistonBehavior.DESTROY),
            false
    );

    public static final class Tags {
        public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, Hollow.id("hollow_logs"));

        public static final TagKey<Block> POLYPORE_PLACEABLE_ON = TagKey.of(RegistryKeys.BLOCK, Hollow.id("polypore_placeable_on"));
    }

    public static <T extends Block> T register(RegistryKey<Block> key, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
        T block = factory.apply(settings);
        if (item) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(
                    Registries.ITEM,
                    itemKey,
                    blockItem
            );
            blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem);
        }

        return Registry.register(Registries.BLOCK, key, block);
    }

    private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
        return register(keyOf(id), factory, settings, true);
    }

    private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
        return register(keyOf(id), factory, settings, item);
    }

    private static Block registerHollowLog(String id, Block base) {
        return register(keyOf(id), HollowLogBlock.of(base), AbstractBlock.Settings.copy(base), true);
    }

    private static Block registerStrippedHollowLog(String id, Block base) {
        return register(keyOf(id), HollowLogBlock.ofStripped(base), AbstractBlock.Settings.copy(base), true);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Hollow.id(id));
    }

    public static void init() {
        // NO-OP
    }
}
