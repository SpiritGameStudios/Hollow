package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.HollowTags;
import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlockRegistrar;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        TagBuilder hollowLogs = getTagBuilder(HollowTags.HOLLOW_LOGS);
        TagBuilder axeMineable = getTagBuilder(BlockTags.AXE_MINEABLE);

        ReflectionHelper.forEachStaticField(
                HollowBlockRegistrar.class,
                HollowLogBlock.class,
                (block, name, field) -> {
                    Identifier id = Registries.BLOCK.getId(block);
                    hollowLogs.add(id);
                    axeMineable.add(id);
                });
    }
}
