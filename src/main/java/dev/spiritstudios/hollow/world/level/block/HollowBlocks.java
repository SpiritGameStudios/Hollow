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
	public static final LogCollection<Block> HOLLOW_LOG = LogCollection.registerBlocks(
		HollowBlockItemIds.HOLLOW_LOG,
		LogCollection.LOGS,
		HollowBlocks::register,
		(log, properties) -> new HollowLogBlock(properties, log, false),
		BlockBehaviour.Properties::ofFullCopy
	);

	public static final LogCollection<Block> STRIPPED_HOLLOW_LOG = LogCollection.registerBlocks(
		HollowBlockItemIds.STRIPPED_HOLLOW_LOG,
		LogCollection.STRIPPED_LOGS,
		HollowBlocks::register,
		(log, properties) -> new HollowLogBlock(properties, log, true),
		BlockBehaviour.Properties::ofFullCopy
	);

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

    public static final Block GIANT_LILY_PAD = register(
            HollowBlockIds.GIANT_LILY_PAD,
            GiantLilyPadBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD)
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

    public static final Block GLASS_JAR = register(
            HollowBlockItemIds.GLASS_JAR,
            GlassJarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.2F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final Block FIREFLY_JAR = register(
            HollowBlockItemIds.FIREFLY_JAR,
            FireflyJarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(GLASS_JAR).lightLevel(state -> 4)
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

    public static void init() {
        FlammableBlockRegistry.getDefaultInstance().add(HollowBlockItemTags.HOLLOW_LOGS.block(), 5, 5);

        OxidizableBlocksRegistry.registerWeatheringCopperBlocks(COPPER_PILLAR);
		LogCollection.zipApply(HOLLOW_LOG, STRIPPED_HOLLOW_LOG, StrippableBlockRegistry::registerCopyState);
    }
}
