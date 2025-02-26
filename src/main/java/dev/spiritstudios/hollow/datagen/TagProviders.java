package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.data.HollowBiomeTags;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowDamageTypes;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class TagProviders {
    public static void addAll(FabricDataGenerator.Pack pack) {
        pack.addProvider(TagProviders.BlockTagProvider::new);
        pack.addProvider(TagProviders.BiomeTagProvider::new);
        pack.addProvider(TagProviders.EntityTypeTagProvider::new);
        pack.addProvider(TagProviders.DamageTypeTagProvider::new);
    }

    private static class BiomeTagProvider extends FabricTagProvider<Biome> {
        public BiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, RegistryKeys.BIOME, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(HollowBiomeTags.HAS_CLOSER_FOG)
                    .forceAddTag(ConventionalBiomeTags.IS_SWAMP);
        }
    }

    private static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
        public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            FabricTagProvider<Block>.FabricTagBuilder hollowLogs = getOrCreateTagBuilder(HollowBlocks.Tags.HOLLOW_LOGS);
            FabricTagProvider<Block>.FabricTagBuilder axeMineable = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE);

            ReflectionHelper.getStaticFields(
                    HollowBlocks.class,
                    HollowLogBlock.class
            ).forEach(pair -> {
                hollowLogs.add(pair.value());
                axeMineable.add(pair.value());
            });

            getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                    .add(HollowBlocks.ECHOING_POT)
                    .add(HollowBlocks.STONE_CHEST)
                    .add(HollowBlocks.STONE_CHEST_LID)
                    .add(HollowBlocks.COPPER_PILLAR)
                    .add(HollowBlocks.WEATHERED_COPPER_PILLAR)
                    .add(HollowBlocks.EXPOSED_COPPER_PILLAR)
                    .add(HollowBlocks.OXIDIZED_COPPER_PILLAR)
                    .add(HollowBlocks.WAXED_COPPER_PILLAR)
                    .add(HollowBlocks.WAXED_WEATHERED_COPPER_PILLAR)
                    .add(HollowBlocks.WAXED_EXPOSED_COPPER_PILLAR)
                    .add(HollowBlocks.WAXED_OXIDIZED_COPPER_PILLAR);

            getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                    .add(HollowBlocks.SCULK_JAW);

            getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                    .add(HollowBlocks.POTTED_ROOTED_ORCHID)
                    .add(HollowBlocks.POTTED_PAEONIA);

            getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                    .add(HollowBlocks.PAEONIA)
                    .add(HollowBlocks.ROOTED_ORCHID)
                    .add(HollowBlocks.LOTUS_LILYPAD);

            getOrCreateTagBuilder(BlockTags.FLOWERS)
                    .add(HollowBlocks.CAMPION);

            getOrCreateTagBuilder(ConventionalBlockTags.TALL_FLOWERS)
                    .add(HollowBlocks.CAMPION);

            getOrCreateTagBuilder(HollowBlocks.Tags.POLYPORE_PLACEABLE_ON)
                    .forceAddTag(BlockTags.LOGS)
                    .addTag(HollowBlocks.Tags.HOLLOW_LOGS);
        }
    }

    private static class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
        public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(HollowEntityTypes.Tags.POISONS_FROG)
                    .add(HollowEntityTypes.FIREFLY);

            getOrCreateTagBuilder(EntityTypeTags.FROG_FOOD)
                    .add(HollowEntityTypes.FIREFLY);

            getOrCreateTagBuilder(HollowEntityTypes.Tags.IMMUNE_TO_SCULK_JAW)
                    .add(EntityType.WARDEN);
        }
    }


    private static class DamageTypeTagProvider extends FabricTagProvider<DamageType> {
        public DamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
                    .add(HollowDamageTypes.SCULK_JAW);

            getOrCreateTagBuilder(DamageTypeTags.NO_KNOCKBACK)
                    .add(HollowDamageTypes.SCULK_JAW);
        }
    }
}
