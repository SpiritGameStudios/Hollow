package dev.callmeecho.hollow.main.registry;

import dev.callmeecho.cabinetapi.block.CabinetBlockSettings;
import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.item.CabinetItemSettings;
import dev.callmeecho.cabinetapi.registry.BlockRegistrar;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static dev.callmeecho.hollow.main.Hollow.GROUP;

@SuppressWarnings("unused")
public class HollowBlockRegistry implements BlockRegistrar {
    public static final HollowLogBlock STRIPPED_OAK_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_OAK_LOG.settings, "stripped_oak_log", "stripped_oak_log", "stripped_oak_log_top");
    public static final HollowLogBlock OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.OAK_LOG.settings).strippedBlock(STRIPPED_OAK_HOLLOW_LOG), "oak_log", "stripped_oak_log", "oak_log_top");
    
    public static final HollowLogBlock STRIPPED_SPRUCE_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_SPRUCE_LOG.settings, "stripped_spruce_log", "stripped_spruce_log", "stripped_spruce_log_top");
    public static final HollowLogBlock SPRUCE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.SPRUCE_LOG.settings).strippedBlock(STRIPPED_SPRUCE_HOLLOW_LOG), "spruce_log", "stripped_spruce_log", "spruce_log_top");
    
    public static final HollowLogBlock STRIPPED_BIRCH_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_BIRCH_LOG.settings, "stripped_birch_log", "stripped_birch_log", "stripped_birch_log_top");
    public static final HollowLogBlock BIRCH_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.BIRCH_LOG.settings).strippedBlock(STRIPPED_BIRCH_HOLLOW_LOG), "birch_log", "stripped_birch_log", "birch_log_top");
    
    public static final HollowLogBlock STRIPPED_JUNGLE_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_JUNGLE_LOG.settings, "stripped_jungle_log", "stripped_jungle_log", "stripped_jungle_log_top");
    public static final HollowLogBlock JUNGLE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.JUNGLE_LOG.settings).strippedBlock(STRIPPED_JUNGLE_HOLLOW_LOG), "jungle_log", "stripped_jungle_log", "jungle_log_top");
    
    public static final HollowLogBlock STRIPPED_ACACIA_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_ACACIA_LOG.settings, "stripped_acacia_log", "stripped_acacia_log", "stripped_acacia_log_top");
    public static final HollowLogBlock ACACIA_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.ACACIA_LOG.settings).strippedBlock(STRIPPED_ACACIA_HOLLOW_LOG), "acacia_log", "stripped_acacia_log", "acacia_log_top");
    
    public static final HollowLogBlock STRIPPED_DARK_OAK_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_DARK_OAK_LOG.settings, "stripped_dark_oak_log", "stripped_dark_oak_log", "stripped_dark_oak_log_top");
    public static final HollowLogBlock DARK_OAK_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.DARK_OAK_LOG.settings).strippedBlock(STRIPPED_DARK_OAK_HOLLOW_LOG), "dark_oak_log", "stripped_dark_oak_log", "dark_oak_log_top");
    
    public static final HollowLogBlock STRIPPED_CRIMSON_HOLLOW_STEM = new HollowLogBlock(Blocks.STRIPPED_CRIMSON_STEM.settings, "stripped_crimson_stem", "stripped_crimson_stem", "stripped_crimson_stem_top");
    public static final HollowLogBlock CRIMSON_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.CRIMSON_STEM.settings).strippedBlock(STRIPPED_CRIMSON_HOLLOW_STEM), "crimson_stem", "stripped_crimson_stem", "crimson_stem_top");
    
    public static final HollowLogBlock STRIPPED_WARPED_HOLLOW_STEM = new HollowLogBlock(Blocks.STRIPPED_WARPED_STEM.settings, "stripped_warped_stem", "stripped_warped_stem", "stripped_warped_stem_top");
    public static final HollowLogBlock WARPED_HOLLOW_STEM = new HollowLogBlock(new CabinetBlockSettings(Blocks.WARPED_STEM.settings).strippedBlock(STRIPPED_WARPED_HOLLOW_STEM), "warped_stem", "stripped_warped_stem", "warped_stem_top");
    
    public static final HollowLogBlock STRIPPED_MANGROVE_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_MANGROVE_LOG.settings, "stripped_mangrove_log", "stripped_mangrove_log", "stripped_mangrove_log_top");
    public static final HollowLogBlock MANGROVE_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.MANGROVE_LOG.settings).strippedBlock(STRIPPED_MANGROVE_HOLLOW_LOG), "mangrove_log", "stripped_mangrove_log", "mangrove_log_top");
    
    public static final HollowLogBlock STRIPPED_CHERRY_HOLLOW_LOG = new HollowLogBlock(Blocks.STRIPPED_CHERRY_LOG.settings, "stripped_cherry_log", "stripped_cherry_log", "stripped_cherry_log_top");
    public static final HollowLogBlock CHERRY_HOLLOW_LOG = new HollowLogBlock(new CabinetBlockSettings(Blocks.CHERRY_LOG.settings).strippedBlock(STRIPPED_CHERRY_HOLLOW_LOG), "cherry_log", "stripped_cherry_log", "cherry_log_top");
    
    public static final FlowerBlock PINK_DAISY = new FlowerBlock(
                    StatusEffects.REGENERATION, 
            8,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.DARK_GREEN)
                            .noCollision()
                            .breakInstantly()
                            .sounds(BlockSoundGroup.GRASS)
                            .offset(AbstractBlock.OffsetType.XZ)
                            .pistonBehavior(PistonBehavior.DESTROY)
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
