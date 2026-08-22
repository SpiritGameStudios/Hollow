package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.tags.HollowBiomeTags;
import dev.spiritstudios.hollow.tags.HollowBlockItemTags;
import dev.spiritstudios.hollow.tags.HollowBlockTags;
import dev.spiritstudios.hollow.tags.HollowEntityTypeTags;
import dev.spiritstudios.hollow.world.entity.HollowDamageTypes;
import dev.spiritstudios.hollow.world.level.block.HollowLogCollection;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HollowTagProviders {
    public static void addAll(FabricDataGenerator.Pack pack) {
        pack.addProvider(HollowTagProviders.BiomeTagProvider::new);
        pack.addProvider(HollowTagProviders.EntityTypeTagProvider::new);
        pack.addProvider(HollowTagProviders.DamageTypeTagProvider::new);
        BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
        pack.addProvider(((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider)));
    }

    private static class BiomeTagProvider extends FabricTagsProvider<Biome> {
        public BiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.BIOME, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(HollowBiomeTags.HAS_CLOSER_FOG)
                    .forceAddTag(ConventionalBiomeTags.IS_SWAMP);
        }
    }

    private static class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
        public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            builder(HollowBlockItemTags.HOLLOW_LOGS.block())
                    .addAll(toIds(HollowBlockItemIds.OAK_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.SPRUCE_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.BIRCH_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.JUNGLE_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.ACACIA_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.CHERRY_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.PALE_OAK_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.DARK_OAK_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.MANGROVE_HOLLOW_LOG))
                    .addAll(toIds(HollowBlockItemIds.CRIMSON_HOLLOW_STEM))
                    .addAll(toIds(HollowBlockItemIds.WARPED_HOLLOW_STEM));

            builder(HollowBlockItemTags.CONTAINS_COLLECTABLE_FIREFLIES.block())
                    .add(BlockItemIds.FIREFLY_BUSH);

            builder(BlockTags.MINEABLE_WITH_AXE)
                    .addTag(HollowBlockItemTags.HOLLOW_LOGS.block());

            builder(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(HollowBlockItemIds.ECHOING_POT)
                    .add(HollowBlockItemIds.STONE_CHEST)
                    .add(HollowBlockItemIds.STONE_CHEST_LID)
                    .addAll(toIds(HollowBlockItemIds.COPPER_PILLAR));

            builder(BlockTags.MINEABLE_WITH_HOE)
                    .add(HollowBlockItemIds.SCULK_JAW);

            builder(BlockTags.SMALL_FLOWERS)
                    .add(HollowBlockItemIds.FLOWERING_LILY_PAD);

            builder(HollowBlockTags.POLYPORE_PLACEABLE_ON)
                    .forceAddTag(BlockTags.LOGS)
                    .addTag(HollowBlockItemTags.HOLLOW_LOGS.block());
        }


        private static WeatheringCopperCollection<ResourceKey<Block>> toIds(final WeatheringCopperCollection<BlockItemId> ids) {
            return ids.map(BlockItemId::block);
        }

        private static List<ResourceKey<Block>> toIds(final HollowLogCollection<BlockItemId> ids) {
            return ids.map(BlockItemId::block).toList();
        }
    }

    private static class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
        public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, @Nullable BlockTagsProvider blockTagsProvider) {
            super(output, registryLookupFuture, blockTagsProvider);
        }

        @Override
        protected void addTags(HolderLookup.Provider registries) {
            builder(ItemTags.SULFUR_CUBE_ARCHETYPE_BOUNCY)
                    .addTag(HollowBlockItemTags.HOLLOW_LOGS.item());

            copy(HollowBlockItemTags.HOLLOW_LOGS.block(), HollowBlockItemTags.HOLLOW_LOGS.item());
            copy(HollowBlockItemTags.CONTAINS_COLLECTABLE_FIREFLIES.block(), HollowBlockItemTags.CONTAINS_COLLECTABLE_FIREFLIES.item());
        }
    }

    private static class EntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
        public EntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(HollowEntityTypeTags.IMMUNE_TO_SCULK_JAW)
                    .add(EntityTypeIds.WARDEN);
        }
    }


    private static class DamageTypeTagProvider extends FabricTagsProvider<DamageType> {
        public DamageTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(DamageTypeTags.BYPASSES_ARMOR)
                    .add(HollowDamageTypes.SCULK_JAW);

            tag(DamageTypeTags.NO_KNOCKBACK)
                    .add(HollowDamageTypes.SCULK_JAW);
        }
    }
}
