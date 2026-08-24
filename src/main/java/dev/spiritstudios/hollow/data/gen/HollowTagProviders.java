package dev.spiritstudios.hollow.data.gen;

import dev.spiritstudios.hollow.references.HollowBlockIds;
import dev.spiritstudios.hollow.references.HollowBlockItemIds;
import dev.spiritstudios.hollow.references.HollowItemIds;
import dev.spiritstudios.hollow.tags.*;
import dev.spiritstudios.hollow.world.entity.HollowDamageTypes;
import dev.spiritstudios.hollow.world.level.block.LogCollection;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockItemTagId;
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
				.addAll(toIds(HollowBlockItemIds.HOLLOW_LOG))
				.addAll(toIds(HollowBlockItemIds.STRIPPED_HOLLOW_LOG));

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

			builder(HollowBlockItemTags.FORMS_GIANT_LILY_PAD.block())
				.add(BlockItemIds.LILY_PAD, HollowBlockItemIds.FLOWERING_LILY_PAD);

			builder(BlockTags.FROG_PREFER_JUMP_TO)
				.add(HollowBlockItemIds.FLOWERING_LILY_PAD.block(), HollowBlockIds.GIANT_LILY_PAD);
		}


		private static WeatheringCopperCollection<ResourceKey<Block>> toIds(final WeatheringCopperCollection<BlockItemId> ids) {
			return ids.map(BlockItemId::block);
		}

		private static List<ResourceKey<Block>> toIds(final LogCollection<BlockItemId> ids) {
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

			builder(ConventionalItemTags.MUSIC_DISCS)
				.add(HollowItemIds.MUSIC_DISC_POSTMORTEM, HollowItemIds.MUSIC_DISC_ONLY_YOU);

			builder(HollowItemTags.CAN_PUT_IN_JAR)
				.forceAddTag(ConventionalItemTags.COOKIE_FOODS)
				.forceAddTag(ConventionalItemTags.MUSIC_DISCS); // will add more

			copy(HollowBlockItemTags.HOLLOW_LOGS);
			copy(HollowBlockItemTags.FORMS_GIANT_LILY_PAD);
		}

		private void copy(BlockItemTagId tag) {
			copy(tag.block(), tag.item());
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
			tag(HollowEntityTypeTags.CAN_CLIMB_HOLLOW_LOGS)
				.add(EntityTypeIds.PLAYER);
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
