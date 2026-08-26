package dev.spiritstudios.hollow.world.item;

import dev.spiritstudios.hollow.core.component.HollowDataComponents;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.references.HollowItemIds;
import dev.spiritstudios.hollow.world.item.component.CopperInstrumentComponent;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.world.level.block.LogCollection;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class HollowItems {
    public static final LogCollection<Item> HOLLOW_LOG = LogCollection.registerBlockItems(
            HollowBlockItemIds.HOLLOW_LOG,
            HollowBlocks.HOLLOW_LOG,
            HollowItems::registerBlock
    );

	public static final LogCollection<Item> STRIPPED_HOLLOW_LOG = LogCollection.registerBlockItems(
		HollowBlockItemIds.STRIPPED_HOLLOW_LOG,
		HollowBlocks.STRIPPED_HOLLOW_LOG,
		HollowItems::registerBlock
	);

    public static final Item ECHOING_POT = registerBlock(
            HollowBlockItemIds.ECHOING_POT,
            HollowBlocks.ECHOING_POT
    );

    public static final Item ECHOING_VASE = registerBlock(
            HollowBlockItemIds.ECHOING_VASE,
            HollowBlocks.ECHOING_VASE
    );

    public static final Item OBABO = registerBlock(
            HollowBlockItemIds.OBABO,
            HollowBlocks.OBABO
    );

    public static final Item SCREAMING_VASE = registerBlock(
            HollowBlockItemIds.SCREAMING_VASE,
            HollowBlocks.SCREAMING_VASE
    );

    public static final Item FLOWERING_LILY_PAD = registerBlock(
            HollowBlockItemIds.FLOWERING_LILY_PAD,
            HollowBlocks.FLOWERING_LILY_PAD,
            PlaceOnWaterBlockItem::new
    );

	public static final Item SWITCHGRASS = registerBlock(HollowBlockItemIds.SWITCHGRASS, HollowBlocks.SWITCHGRASS);

    public static final Item CATTAIL = registerBlock(HollowBlockItemIds.CATTAIL, HollowBlocks.CATTAIL);

    public static final Item POLYPORE = registerBlock(HollowBlockItemIds.POLYPORE, HollowBlocks.POLYPORE);


    public static final Item SCULK_JAW = registerBlock(HollowBlockItemIds.SCULK_JAW, HollowBlocks.SCULK_JAW);

    public static final Item GLASS_JAR = registerBlock(HollowBlockItemIds.GLASS_JAR, HollowBlocks.GLASS_JAR, JarItem::new);

    public static final Item FIREFLY_JAR = registerBlock(HollowBlockItemIds.FIREFLY_JAR, HollowBlocks.FIREFLY_JAR);

    public static final Item STONE_CHEST = registerBlock(HollowBlockItemIds.STONE_CHEST, HollowBlocks.STONE_CHEST);

    public static final Item STONE_CHEST_LID = registerBlock(HollowBlockItemIds.STONE_CHEST_LID, HollowBlocks.STONE_CHEST_LID);

    public static final WeatheringCopperCollection<Item> COPPER_PILLAR = WeatheringCopperCollection.registerItems(
            HollowBlockItemIds.COPPER_PILLAR,
            HollowBlocks.COPPER_PILLAR,
            HollowItems::registerBlock
    );


    public static final Item MUSIC_DISC_POSTMORTEM = registerItem(
            HollowItemIds.MUSIC_DISC_POSTMORTEM,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
                    .jukeboxPlayable(HollowJukeboxSongs.POSTMORTEM)
    );

	public static final Item MUSIC_DISC_ONLY_YOU = registerItem(
		HollowItemIds.MUSIC_DISC_ONLY_YOU,
		new Item.Properties()
			.stacksTo(1)
			.rarity(Rarity.UNCOMMON)
			.jukeboxPlayable(HollowJukeboxSongs.ONLY_YOU)
	);

    public static final Item COPPER_HORN = registerItem(
            HollowItemIds.COPPER_HORN,
            CopperHornItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .delayedComponent(
                            HollowDataComponents.COPPER_INSTRUMENT,
                            context -> new CopperInstrumentComponent(context.getOrThrow(CopperInstruments.GREAT_SKY_FALLING))
                    )
    );


    private static Item registerItem(final ResourceKey<Item> id, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(id));
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    private static Item registerItem(final ResourceKey<Item> id, final Function<Item.Properties, Item> itemFactory) {
        return registerItem(id, itemFactory, new Item.Properties());
    }

    private static Item registerItem(final ResourceKey<Item> id, final Item.Properties properties) {
        return registerItem(id, Item::new, properties);
    }

    private static Item registerItem(final ResourceKey<Item> id) {
        return registerItem(id, Item::new, new Item.Properties());
    }

    private static Item registerSpawnEgg(final ResourceKey<Item> id, final EntityType<?> type) {
        return registerItem(id, SpawnEggItem::new, new Item.Properties().spawnEgg(type));
    }

    private static Item registerBlock(
            final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties
    ) {
        return registerItem(id.item(), p -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    private static Item registerBlock(final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory) {
        return registerBlock(id, block, itemFactory, new Item.Properties());
    }

    private static Item registerBlock(final BlockItemId id, final Block block) {
        return registerBlock(id, block, BlockItem::new, new Item.Properties());
    }

    public static void init() {
        CompostableRegistry.INSTANCE.add(POLYPORE, 0.65F);
        CompostableRegistry.INSTANCE.add(CATTAIL, 0.65F);
        CompostableRegistry.INSTANCE.add(FLOWERING_LILY_PAD, 0.75F);
        // CompostableRegistry.INSTANCE.add(GIANT_LILY_PAD, 0.85F);
    }
}
