package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.registry.BlockRegistrar;
import dev.callmeecho.hollow.main.block.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import static dev.callmeecho.hollow.main.Hollow.GROUP;

@SuppressWarnings("unused")
public class HollowBlockRegistrar implements BlockRegistrar {
    public static final HollowLogBlock STRIPPED_OAK_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG).flammable(), "stripped_oak_log", "stripped_oak_log", "stripped_oak_log_top");
    public static final HollowLogBlock OAK_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG).strippedBlock(STRIPPED_OAK_HOLLOW_LOG).flammable(), "oak_log", "stripped_oak_log", "oak_log_top");

    public static final HollowLogBlock STRIPPED_SPRUCE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_SPRUCE_LOG).flammable(), "stripped_spruce_log", "stripped_spruce_log", "stripped_spruce_log_top");
    public static final HollowLogBlock SPRUCE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_LOG).strippedBlock(STRIPPED_SPRUCE_HOLLOW_LOG).flammable(), "spruce_log", "stripped_spruce_log", "spruce_log_top");

    public static final HollowLogBlock STRIPPED_BIRCH_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_BIRCH_LOG).flammable(), "stripped_birch_log", "stripped_birch_log", "stripped_birch_log_top");
    public static final HollowLogBlock BIRCH_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.BIRCH_LOG).strippedBlock(STRIPPED_BIRCH_HOLLOW_LOG).flammable(), "birch_log", "stripped_birch_log", "birch_log_top");

    public static final HollowLogBlock STRIPPED_JUNGLE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_JUNGLE_LOG).flammable(), "stripped_jungle_log", "stripped_jungle_log", "stripped_jungle_log_top");
    public static final HollowLogBlock JUNGLE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_LOG).strippedBlock(STRIPPED_JUNGLE_HOLLOW_LOG).flammable(), "jungle_log", "stripped_jungle_log", "jungle_log_top");

    public static final HollowLogBlock STRIPPED_ACACIA_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_ACACIA_LOG).flammable(), "stripped_acacia_log", "stripped_acacia_log", "stripped_acacia_log_top");
    public static final HollowLogBlock ACACIA_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_LOG).strippedBlock(STRIPPED_ACACIA_HOLLOW_LOG).flammable(), "acacia_log", "stripped_acacia_log", "acacia_log_top");

    public static final HollowLogBlock STRIPPED_DARK_OAK_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_DARK_OAK_LOG).flammable(), "stripped_dark_oak_log", "stripped_dark_oak_log", "stripped_dark_oak_log_top");
    public static final HollowLogBlock DARK_OAK_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.DARK_OAK_LOG).strippedBlock(STRIPPED_DARK_OAK_HOLLOW_LOG).flammable(), "dark_oak_log", "stripped_dark_oak_log", "dark_oak_log_top");

    public static final HollowLogBlock STRIPPED_CRIMSON_HOLLOW_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CRIMSON_STEM), "stripped_crimson_stem", "stripped_crimson_stem", "stripped_crimson_stem_top");
    public static final HollowLogBlock CRIMSON_HOLLOW_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_STEM).strippedBlock(STRIPPED_CRIMSON_HOLLOW_STEM), "crimson_stem", "stripped_crimson_stem", "crimson_stem_top");

    public static final HollowLogBlock STRIPPED_WARPED_HOLLOW_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_WARPED_STEM), "stripped_warped_stem", "stripped_warped_stem", "stripped_warped_stem_top");
    public static final HollowLogBlock WARPED_HOLLOW_STEM = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.WARPED_STEM).strippedBlock(STRIPPED_WARPED_HOLLOW_STEM), "warped_stem", "stripped_warped_stem", "warped_stem_top");

    public static final HollowLogBlock STRIPPED_MANGROVE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_MANGROVE_LOG).flammable(), "stripped_mangrove_log", "stripped_mangrove_log", "stripped_mangrove_log_top");
    public static final HollowLogBlock MANGROVE_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.MANGROVE_LOG).strippedBlock(STRIPPED_MANGROVE_HOLLOW_LOG).flammable(), "mangrove_log", "stripped_mangrove_log", "mangrove_log_top");

    public static final HollowLogBlock STRIPPED_CHERRY_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_CHERRY_LOG).flammable(), "stripped_cherry_log", "stripped_cherry_log", "stripped_cherry_log_top");
    public static final HollowLogBlock CHERRY_HOLLOW_LOG = new HollowLogBlock(AbstractBlock.Settings.copy(Blocks.CHERRY_LOG).strippedBlock(STRIPPED_CHERRY_HOLLOW_LOG).flammable(), "cherry_log", "stripped_cherry_log", "cherry_log_top");

    public static final EchoingPotBlock ECHOING_POT = new EchoingPotBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.POLISHED_DEEPSLATE)
    );
    
    public static final FlowerBlock PAEONIA = new FlowerBlock(
            StatusEffects.GLOWING,
            5,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final FlowerBlock ROOTED_ORCHID = new FlowerBlock(
            StatusEffects.SATURATION,
            0.35F,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    
    public static final TallFlowerBlock CAMPION = new TallFlowerBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .flammable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    ) {
        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            if (state.get(HALF) == DoubleBlockHalf.UPPER) return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
            return super.getOutlineShape(state, world, pos, context);
        }
    };
    
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

    public static final RootVinesBlock ROOT_VINES = new RootVinesBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.HANGING_ROOTS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final CattailBlock CATTAIL = new CattailBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );


    @NoBlockItem
    public static final LilyPadBlock LOTUS_LILYPAD = new LilyPadBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .breakInstantly()
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    @NoBlockItem
    public static final Block GIANT_LILYPAD = new GiantLilyPadBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .breakInstantly()
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    
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
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.SCULK)
    );
    
    public static final Block JAR = new JarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block FIREFLY_JAR = new FireflyJarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .luminance(state -> 15)
    );
    
    public static final Block STONE_CHEST = new StoneChestBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(6.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final Block STONE_CHEST_LID = new StoneChestLidBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );

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
    
    @NoBlockItem
    public static final Block POTTED_PAEONIA = Blocks.createFlowerPotBlock(PAEONIA);

    @NoBlockItem
    public static final Block POTTED_ROOTED_ORCHID = Blocks.createFlowerPotBlock(ROOTED_ORCHID);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerBlockItem(Block block, String namespace, String name) {
        BlockItem item = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, Identifier.of(namespace, name), item);

        GROUP.addItem(item);
    }
}
