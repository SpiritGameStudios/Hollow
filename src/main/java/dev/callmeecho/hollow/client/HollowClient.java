package dev.callmeecho.hollow.client;

import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.client.render.entity.FireflyEntityRenderer;
import dev.callmeecho.hollow.main.Hollow;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.registry.HollowBlockRegistry;
import dev.callmeecho.hollow.main.registry.HollowEntityTypeRegistry;
import dev.callmeecho.hollow.main.registry.HollowItemRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.mixin.client.rendering.BlockColorsMixin;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class HollowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(HollowEntityTypeRegistry.FIREFLY, FireflyEntityRenderer::new);
        
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.PAEONIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.LOTUS_LILYPAD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.TWIG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.CAMPION, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.POLYPORE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.POTTED_PAEONIA, RenderLayer.getCutout());

        RegistrarHandler.<HollowLogBlock>forEach(HollowBlockRegistry.class, (block) -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped()));


        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? 0x208030 : 0x71C35C, HollowBlockRegistry.LOTUS_LILYPAD);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x71C35C : -1, HollowItemRegistry.LOTUS_LILYPAD);
    }
}
