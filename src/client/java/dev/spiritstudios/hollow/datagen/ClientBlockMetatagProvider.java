package dev.spiritstudios.hollow.datagen;

import dev.spiritstudios.hollow.block.HollowLogBlock;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import dev.spiritstudios.specter.api.render.BlockRenderLayer;
import dev.spiritstudios.specter.api.render.RenderMetatags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ClientBlockMetatagProvider extends MetatagProvider<Block> {
    protected ClientBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.RESOURCE_PACK);
    }

    @Override
    protected void configure(Consumer<MetatagBuilder<Block, ?>> provider, RegistryWrapper.WrapperLookup lookup) {
        MetatagBuilder<Block, BlockRenderLayer> renderLayer = create(RenderMetatags.RENDER_LAYER);

        ReflectionHelper.getStaticFields(
                HollowBlocks.class,
                HollowLogBlock.class
        ).forEach(pair ->
                renderLayer.put(pair.value(), BlockRenderLayer.CUTOUT_MIPPED));

        renderLayer.put(HollowBlocks.PAEONIA, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.POTTED_PAEONIA, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.ROOTED_ORCHID, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.POTTED_ROOTED_ORCHID, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.CAMPION, BlockRenderLayer.CUTOUT);

        renderLayer.put(HollowBlocks.LOTUS_LILYPAD, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.GIANT_LILYPAD, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.SUPER_GIANT_LILYPAD, BlockRenderLayer.CUTOUT);

        renderLayer.put(HollowBlocks.TWIG, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.POLYPORE, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.CATTAIL, BlockRenderLayer.CUTOUT);
        renderLayer.put(HollowBlocks.CATTAIL_STEM, BlockRenderLayer.CUTOUT);

        renderLayer.put(HollowBlocks.JAR, BlockRenderLayer.CUTOUT_MIPPED);
        renderLayer.put(HollowBlocks.FIREFLY_JAR, BlockRenderLayer.CUTOUT_MIPPED);

        provider.accept(renderLayer);
    }

    @Override
    public String getName() {
        return super.getName() + " for minecraft:block (Client)";
    }
}
