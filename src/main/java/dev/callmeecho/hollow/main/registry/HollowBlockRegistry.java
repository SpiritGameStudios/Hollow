package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.block.CabinetBlockSettings;
import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.BlockRegistrar;
import dev.callmeecho.hollow.main.block.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.Instrument;
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
public class HollowBlockRegistry implements BlockRegistrar {
    public static final HollowLogBlock STRIPPED_OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_OAK_LOG.settings).flammable(), "stripped_oak_log", "stripped_oak_log", "stripped_oak_log_top");
    public static final HollowLogBlock OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.OAK_LOG.settings).strippedBlock(STRIPPED_OAK_HOLLOW_LOG).flammable(), "oak_log", "stripped_oak_log", "oak_log_top");

    public static final HollowLogBlock STRIPPED_SPRUCE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_SPRUCE_LOG.settings).flammable(), "stripped_spruce_log", "stripped_spruce_log", "stripped_spruce_log_top");
    public static final HollowLogBlock SPRUCE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.SPRUCE_LOG.settings).strippedBlock(STRIPPED_SPRUCE_HOLLOW_LOG).flammable(), "spruce_log", "stripped_spruce_log", "spruce_log_top");

    public static final HollowLogBlock STRIPPED_BIRCH_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_BIRCH_LOG.settings).flammable(), "stripped_birch_log", "stripped_birch_log", "stripped_birch_log_top");
    public static final HollowLogBlock BIRCH_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.BIRCH_LOG.settings).strippedBlock(STRIPPED_BIRCH_HOLLOW_LOG).flammable(), "birch_log", "stripped_birch_log", "birch_log_top");

    public static final HollowLogBlock STRIPPED_JUNGLE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_JUNGLE_LOG.settings).flammable(), "stripped_jungle_log", "stripped_jungle_log", "stripped_jungle_log_top");
    public static final HollowLogBlock JUNGLE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.JUNGLE_LOG.settings).strippedBlock(STRIPPED_JUNGLE_HOLLOW_LOG).flammable(), "jungle_log", "stripped_jungle_log", "jungle_log_top");

    public static final HollowLogBlock STRIPPED_ACACIA_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_ACACIA_LOG.settings).flammable(), "stripped_acacia_log", "stripped_acacia_log", "stripped_acacia_log_top");
    public static final HollowLogBlock ACACIA_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.ACACIA_LOG.settings).strippedBlock(STRIPPED_ACACIA_HOLLOW_LOG).flammable(), "acacia_log", "stripped_acacia_log", "acacia_log_top");

    public static final HollowLogBlock STRIPPED_DARK_OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_DARK_OAK_LOG.settings).flammable(), "stripped_dark_oak_log", "stripped_dark_oak_log", "stripped_dark_oak_log_top");
    public static final HollowLogBlock DARK_OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.DARK_OAK_LOG.settings).strippedBlock(STRIPPED_DARK_OAK_HOLLOW_LOG).flammable(), "dark_oak_log", "stripped_dark_oak_log", "dark_oak_log_top");

    public static final HollowLogBlock STRIPPED_CRIMSON_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_CRIMSON_STEM.settings), "stripped_crimson_stem", "stripped_crimson_stem", "stripped_crimson_stem_top");
    public static final HollowLogBlock CRIMSON_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.CRIMSON_STEM.settings).strippedBlock(STRIPPED_CRIMSON_HOLLOW_STEM), "crimson_stem", "stripped_crimson_stem", "crimson_stem_top");

    public static final HollowLogBlock STRIPPED_WARPED_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_WARPED_STEM.settings), "stripped_warped_stem", "stripped_warped_stem", "stripped_warped_stem_top");
    public static final HollowLogBlock WARPED_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.WARPED_STEM.settings).strippedBlock(STRIPPED_WARPED_HOLLOW_STEM), "warped_stem", "stripped_warped_stem", "warped_stem_top");

    public static final HollowLogBlock STRIPPED_MANGROVE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_MANGROVE_LOG.settings).flammable(), "stripped_mangrove_log", "stripped_mangrove_log", "stripped_mangrove_log_top");
    public static final HollowLogBlock MANGROVE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.MANGROVE_LOG.settings).strippedBlock(STRIPPED_MANGROVE_HOLLOW_LOG).flammable(), "mangrove_log", "stripped_mangrove_log", "mangrove_log_top");

    public static final HollowLogBlock STRIPPED_CHERRY_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.STRIPPED_CHERRY_LOG.settings).flammable(), "stripped_cherry_log", "stripped_cherry_log", "stripped_cherry_log_top");
    public static final HollowLogBlock CHERRY_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.CHERRY_LOG.settings).strippedBlock(STRIPPED_CHERRY_HOLLOW_LOG).flammable(), "cherry_log", "stripped_cherry_log", "cherry_log_top");

    public static final EchoingPotBlock ECHOING_POT = new EchoingPotBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(0.2F)
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
    
    public static final TallFlowerBlock CAMPION = new TallFlowerBlock(
            CabinetBlockSettings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .flammable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    ) {
        @SuppressWarnings("deprecation")
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
    
    @NoBlockItem
    public static final LilyPadBlock LOTUS_LILYPAD = new LilyPadBlock(
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
                    .instrument(Instrument.BASEDRUM)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final Block STONE_CHEST_LID = new StoneChestLidBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .instrument(Instrument.BASEDRUM)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    
    @NoBlockItem
    public static final FlowerPotBlock POTTED_PAEONIA = Blocks.createFlowerPotBlock(PAEONIA);

    @Override
    public void registerBlockItem(Block block, String namespace, String name) {
        BlockItem item = new BlockItem(block, new Item.Settings());

        Registry.register(Registries.ITEM, new Identifier(namespace, name), item);

        if (block.settings instanceof CabinetBlockSettings settings) {
            CabinetItemGroup group = settings.getGroup();
            if (group != null) {
                group.addItem(item);
                return;
            }
        }

        // Yes, I could have set group in block settings, but seeing as every block here has the same group, this is easier.
        // TODO: Add a way to set group for every block in a Registrar
        GROUP.addItem(item);
    }
}
