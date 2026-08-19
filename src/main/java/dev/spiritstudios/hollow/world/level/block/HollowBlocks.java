package dev.spiritstudios.hollow.world.level.block;

import dev.spiritstudios.hollow.references.HollowBlockIds;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

@SuppressWarnings("unused")
public final class HollowBlocks {
    // region Hollow logs
    public static final HollowLogCollection<Block> OAK_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.OAK_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG
    );

    public static final HollowLogCollection<Block> SPRUCE_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.SPRUCE_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG
    );

    public static final HollowLogCollection<Block> BIRCH_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.BIRCH_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG
    );

    public static final HollowLogCollection<Block> JUNGLE_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.JUNGLE_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG
    );

    public static final HollowLogCollection<Block> ACACIA_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.ACACIA_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG
    );

    public static final HollowLogCollection<Block> DARK_OAK_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.DARK_OAK_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG
    );

    public static final HollowLogCollection<Block> CRIMSON_HOLLOW_STEM = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.CRIMSON_HOLLOW_STEM,
            HollowBlocks::register,
            Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM
    );

    public static final HollowLogCollection<Block> WARPED_HOLLOW_STEM = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.WARPED_HOLLOW_STEM,
            HollowBlocks::register,
            Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM
    );

    public static final HollowLogCollection<Block> MANGROVE_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.MANGROVE_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG
    );

    public static final HollowLogCollection<Block> CHERRY_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.CHERRY_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG
    );

    public static final HollowLogCollection<Block> PALE_OAK_HOLLOW_LOG = HollowLogCollection.registerBlocks(
            HollowBlockItemIds.PALE_OAK_HOLLOW_LOG,
            HollowBlocks::register,
            Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG
    );

    // endregion

    public static final Block ECHOING_POT = register(
            HollowBlockItemIds.ECHOING_POT,
            EchoingPotBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.DECORATED_POT)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()
    );

    public static final Block ECHOING_VASE = registerCopyOf(
            HollowBlockItemIds.ECHOING_VASE,
            EchoingVaseBlock::new,
            ECHOING_POT
    );

    public static final Block OBABO = registerCopyOf(
            HollowBlockItemIds.OBABO,
            EchoingVaseBlock.ObaboBlock::new,
            ECHOING_POT
    );

    public static final Block SCREAMING_VASE = registerCopyOf(
            HollowBlockItemIds.SCREAMING_VASE,
            ScreamingVaseBlock::new,
            ECHOING_POT
    );

    public static final Block CATTAIL_STEM = register(
            HollowBlockIds.CATTAIL_STEM,
            CattailStemBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .replaceable()
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WET_GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final CattailBlock CATTAIL = registerCopyOf(
            HollowBlockItemIds.CATTAIL,
            CattailBlock::new,
            CATTAIL_STEM
    );

	public static final SwitchgrassBlock SWITCHGRASS = register(
		HollowBlockItemIds.SWITCHGRASS,
		SwitchgrassBlock::new,
		BlockBehaviour.Properties.ofFullCopy(Blocks.FIREFLY_BUSH)
			.lightLevel(state -> 0)
			.randomTicks()
	);

    public static final Block FLOWERING_LILY_PAD = registerCopyOf(
            HollowBlockItemIds.FLOWERING_LILY_PAD,
            LilyPadBlock::new,
            Blocks.LILY_PAD
    );

    public static final Block GIANT_LILY_PAD = registerCopyOf(
            HollowBlockItemIds.GIANT_LILY_PAD,
            GiantLilyPadBlock::new,
            Blocks.LILY_PAD
    );

    public static final Block POLYPORE = register(
            HollowBlockItemIds.POLYPORE,
            PolyporeBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(Blocks::never)
    );

    public static final Block SCULK_JAW = register(
            HollowBlockItemIds.SCULK_JAW,
            SculkJawBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                    .dynamicShape()
                    .speedFactor(0.25F)
                    .jumpFactor(0.1F)
                    .strength(3.0F, 3.0F)
                    .lightLevel(state -> state.getValue(SculkJawBlock.ACTIVE) ? 6 : 0)
    );

    public static final Block JAR = register(
            HollowBlockItemIds.JAR,
            JarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.2F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final Block JAR_OF_FIREFLIES = register(
            HollowBlockItemIds.JAR_OF_FIREFLIES,
            FireflyJarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(JAR).lightLevel(state -> 4)
    );

    public static final Block STONE_CHEST = register(
            HollowBlockItemIds.STONE_CHEST,
            StoneChestBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(6.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE)
    );

    public static final Block STONE_CHEST_LID = register(
            HollowBlockItemIds.STONE_CHEST_LID,
            StoneChestLidBlock::new,
            BlockBehaviour.Properties.ofFullCopy(STONE_CHEST).strength(3.0F, 6.0F)
    );

    public static final WeatheringCopperCollection<Block> COPPER_PILLAR = WeatheringCopperCollection.registerBlocks(
            HollowBlockItemIds.COPPER_PILLAR,
            HollowBlocks::register,
            (_, properties) -> new RotatedPillarBlock(properties),
            OxidizablePillarBlock::new,
            w -> BlockBehaviour.Properties.of()
                    .mapColor(switch (w) {
                        case UNAFFECTED -> MapColor.COLOR_ORANGE;
                        case EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY;
                        case WEATHERED -> MapColor.WARPED_STEM;
                        case OXIDIZED -> MapColor.WARPED_NYLIUM;
                    })
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .lightLevel(_ -> 15)
                    .sound(SoundType.COPPER)
    );

    private static <T extends Block> T register(final ResourceKey<Block> id, final Function<BlockBehaviour.Properties, T> factory, final BlockBehaviour.Properties properties) {
        T block = factory.apply(properties.setId(id));
        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static <T extends Block> T register(final BlockItemId id, final Function<BlockBehaviour.Properties, T> factory, final BlockBehaviour.Properties properties) {
        return register(id.block(), factory, properties);
    }

    private static Block register(final BlockItemId id, final BlockBehaviour.Properties properties) {
        return register(id, Block::new, properties);
    }

    private static Block registerCopyOf(final BlockItemId id, final Block parent) {
        return register(id, BlockBehaviour.Properties.ofFullCopy(parent));
    }

    private static <T extends Block> T registerCopyOf(final BlockItemId id, final Function<BlockBehaviour.Properties, T> factory, final Block parent) {
        return register(id, factory, BlockBehaviour.Properties.ofFullCopy(parent));
    }

    private static void registerStripping(HollowLogCollection<Block> collection) {
        StrippableBlockRegistry.registerCopyState(collection.hollowLog(), collection.strippedHollowLog());
    }

    public static void init() {
        FlammableBlockRegistry.getDefaultInstance().add(HollowBlockItemTags.HOLLOW_LOGS.block(), 5, 5);

        OxidizableBlocksRegistry.registerWeatheringCopperBlocks(COPPER_PILLAR);

        registerStripping(HollowBlocks.OAK_HOLLOW_LOG);
        registerStripping(HollowBlocks.SPRUCE_HOLLOW_LOG);
        registerStripping(HollowBlocks.BIRCH_HOLLOW_LOG);
        registerStripping(HollowBlocks.JUNGLE_HOLLOW_LOG);
        registerStripping(HollowBlocks.ACACIA_HOLLOW_LOG);
        registerStripping(HollowBlocks.DARK_OAK_HOLLOW_LOG);
        registerStripping(HollowBlocks.CRIMSON_HOLLOW_STEM);
        registerStripping(HollowBlocks.WARPED_HOLLOW_STEM);
        registerStripping(HollowBlocks.MANGROVE_HOLLOW_LOG);
        registerStripping(HollowBlocks.CHERRY_HOLLOW_LOG);
        registerStripping(HollowBlocks.PALE_OAK_HOLLOW_LOG);
    }
}
