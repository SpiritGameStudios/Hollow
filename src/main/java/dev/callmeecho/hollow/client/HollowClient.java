package dev.callmeecho.hollow.client;

import dev.callmeecho.cabinetapi.misc.ReflectionHelper;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import dev.callmeecho.hollow.client.render.entity.FireflyEntityRenderer;
import dev.callmeecho.hollow.client.render.entity.JarBlockEntityRenderer;
import dev.callmeecho.hollow.main.Hollow;
import dev.callmeecho.hollow.main.block.HollowLogBlock;
import dev.callmeecho.hollow.main.particle.FireflyJarParticle;
import dev.callmeecho.hollow.main.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

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
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.JAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.FIREFLY_JAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(HollowBlockRegistry.ROOT_VINES, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(HollowBlockEntityRegistry.JAR_BLOCK_ENTITY, JarBlockEntityRenderer::new);

        ReflectionHelper.forEachStaticField(HollowBlockRegistry.class, HollowLogBlock.class, (block, name, field) -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped()));

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? 0x208030 : 0x71C35C, HollowBlockRegistry.LOTUS_LILYPAD);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x71C35C : -1, HollowItemRegistry.LOTUS_LILYPAD);

        ParticleFactoryRegistry.getInstance().register(HollowParticleRegistrar.FIREFLY_JAR, FireflyJarParticle.Factory::new);
    }
}