package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.block.*;
import dev.spiritstudios.specter.api.registry.annotations.NoBlockItem;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;

@SuppressWarnings("unused")
public final class HollowBlocks {
    // region Hollow logs
    public static final HollowLogBlock OAK_HOLLOW_LOG = HollowLogBlock.of(Blocks.OAK_LOG);
    public static final HollowLogBlock STRIPPED_OAK_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_OAK_LOG);

    public static final HollowLogBlock SPRUCE_HOLLOW_LOG = HollowLogBlock.of(Blocks.SPRUCE_LOG);
    public static final HollowLogBlock STRIPPED_SPRUCE_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_SPRUCE_LOG);

    public static final HollowLogBlock BIRCH_HOLLOW_LOG = HollowLogBlock.of(Blocks.BIRCH_LOG);
    public static final HollowLogBlock STRIPPED_BIRCH_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_BIRCH_LOG);

    public static final HollowLogBlock JUNGLE_HOLLOW_LOG = HollowLogBlock.of(Blocks.JUNGLE_LOG);
    public static final HollowLogBlock STRIPPED_JUNGLE_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_JUNGLE_LOG);

    public static final HollowLogBlock ACACIA_HOLLOW_LOG = HollowLogBlock.of(Blocks.ACACIA_LOG);
    public static final HollowLogBlock STRIPPED_ACACIA_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_ACACIA_LOG);

    public static final HollowLogBlock DARK_OAK_HOLLOW_LOG = HollowLogBlock.of(Blocks.DARK_OAK_LOG);
    public static final HollowLogBlock STRIPPED_DARK_OAK_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_DARK_OAK_LOG);

    public static final HollowLogBlock CRIMSON_HOLLOW_STEM = HollowLogBlock.of(Blocks.CRIMSON_STEM);
    public static final HollowLogBlock STRIPPED_CRIMSON_HOLLOW_STEM = HollowLogBlock.ofStripped(Blocks.STRIPPED_CRIMSON_STEM);

    public static final HollowLogBlock WARPED_HOLLOW_STEM = HollowLogBlock.of(Blocks.WARPED_STEM);
    public static final HollowLogBlock STRIPPED_WARPED_HOLLOW_STEM = HollowLogBlock.ofStripped(Blocks.STRIPPED_WARPED_STEM);

    public static final HollowLogBlock MANGROVE_HOLLOW_LOG = HollowLogBlock.of(Blocks.MANGROVE_LOG);
    public static final HollowLogBlock STRIPPED_MANGROVE_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_MANGROVE_LOG);

    public static final HollowLogBlock CHERRY_HOLLOW_LOG = HollowLogBlock.of(Blocks.CHERRY_LOG);
    public static final HollowLogBlock STRIPPED_CHERRY_HOLLOW_LOG = HollowLogBlock.ofStripped(Blocks.STRIPPED_CHERRY_LOG);
    // endregion

    public static final EchoingPotBlock ECHOING_POT = new EchoingPotBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DECORATED_POT)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );

    public static final FlowerBlock PAEONIA = new FlowerBlock(
            StatusEffects.GLOWING,
            5,
            AbstractBlock.Settings.copy(Blocks.ALLIUM)
    );

    public static final FlowerBlock ROOTED_ORCHID = new FlowerBlock(
            StatusEffects.SATURATION,
            0.35F,
            AbstractBlock.Settings.copy(Blocks.BLUE_ORCHID)
    );

    public static final TallFlowerBlock CAMPION = new CampionBlock(AbstractBlock.Settings.copy(Blocks.PEONY));

    public static final TwigBlock TWIG = new TwigBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    @NoBlockItem
    public static final CattailStemBlock CATTAIL_STEM = new CattailStemBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final CattailBlock CATTAIL = new CattailBlock(AbstractBlock.Settings.copy(CATTAIL_STEM));

    @NoBlockItem
    public static final LilyPadBlock LOTUS_LILYPAD = new LilyPadBlock(AbstractBlock.Settings.copy(Blocks.LILY_PAD));

    @NoBlockItem
    public static final Block GIANT_LILYPAD = new GiantLilyPadBlock(AbstractBlock.Settings.copy(Blocks.LILY_PAD));

    public static final PolyporeBlock POLYPORE = new PolyporeBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
    );

    public static final Block SCULK_JAW = new SculkJawBlock(
            AbstractBlock.Settings.copy(Blocks.SCULK)
                    .velocityMultiplier(0.25F)
                    .jumpVelocityMultiplier(0.0F)
                    .strength(3.0F, 3.0F)
                    .luminance(state -> state.get(SculkJawBlock.ACTIVE) ? 6 : 0)
    );

    public static final Block JAR = new JarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block FIREFLY_JAR = new FireflyJarBlock(AbstractBlock.Settings.copy(JAR)
            .luminance(state -> 15));

    public static final Block STONE_CHEST = new StoneChestBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(6.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final Block STONE_CHEST_LID = new StoneChestLidBlock(AbstractBlock.Settings.copy(STONE_CHEST)
            .strength(3.0F, 6.0F));

    // region Copper Pillar
    public static final Block COPPER_PILLAR = new OxidizablePillarBlock(
            Oxidizable.OxidationLevel.UNAFFECTED,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
                    .strength(3.0F, 6.0F)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.COPPER)
    );

    public static final Block EXPOSED_COPPER_PILLAR = new OxidizablePillarBlock(
            Oxidizable.OxidationLevel.EXPOSED,
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );

    public static final Block WEATHERED_COPPER_PILLAR = new OxidizablePillarBlock(
            Oxidizable.OxidationLevel.WEATHERED,
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.DARK_AQUA)
    );

    public static final Block OXIDIZED_COPPER_PILLAR = new OxidizablePillarBlock(
            Oxidizable.OxidationLevel.OXIDIZED,
            AbstractBlock.Settings.copy(COPPER_PILLAR).mapColor(MapColor.TEAL)
    );

    public static final Block WAXED_COPPER_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(COPPER_PILLAR));
    public static final Block WAXED_EXPOSED_COPPER_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(EXPOSED_COPPER_PILLAR));
    public static final Block WAXED_WEATHERED_COPPER_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(WEATHERED_COPPER_PILLAR));
    public static final Block WAXED_OXIDIZED_COPPER_PILLAR = new PillarBlock(AbstractBlock.Settings.copy(OXIDIZED_COPPER_PILLAR));
    // endregion

    @NoBlockItem
    public static final Block POTTED_PAEONIA = Blocks.createFlowerPotBlock(PAEONIA);

    @NoBlockItem
    public static final Block POTTED_ROOTED_ORCHID = Blocks.createFlowerPotBlock(ROOTED_ORCHID);

    // region Wildflowers
    public static final Block PINK_WILDFLOWER = new FlowerbedBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.PINK)
            .noCollision()
            .sounds(BlockSoundGroup.PINK_PETALS)
            .pistonBehavior(PistonBehavior.DESTROY));

    public static final Block BLUE_WILDFLOWER = new FlowerbedBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_BLUE)
            .noCollision()
            .sounds(BlockSoundGroup.PINK_PETALS)
            .pistonBehavior(PistonBehavior.DESTROY));

    public static final Block PURPLE_WILDFLOWER = new FlowerbedBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.PURPLE)
            .noCollision()
            .sounds(BlockSoundGroup.PINK_PETALS)
            .pistonBehavior(PistonBehavior.DESTROY));

    public static final Block WHITE_WILDFLOWER = new FlowerbedBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.WHITE_GRAY)
            .noCollision()
            .sounds(BlockSoundGroup.PINK_PETALS)
            .pistonBehavior(PistonBehavior.DESTROY));
    // endregion

    public static final class Tags {
        public static final TagKey<Block> HOLLOW_LOGS = TagKey.of(RegistryKeys.BLOCK, Hollow.id("hollow_logs"));

        public static final TagKey<Block> POLYPORE_PLACEABLE_ON = TagKey.of(RegistryKeys.BLOCK, Hollow.id("polypore_placeable_on"));
    }
}
