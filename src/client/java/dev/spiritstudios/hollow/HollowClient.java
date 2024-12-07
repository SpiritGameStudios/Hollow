package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.particle.FireflyJarParticle;
import dev.spiritstudios.hollow.registry.*;
import dev.spiritstudios.hollow.render.block.EchoingPotBlockEntityRenderer;
import dev.spiritstudios.hollow.render.entity.FireflyEntityRenderer;
import dev.spiritstudios.hollow.render.entity.JarBlockEntityRenderer;
import dev.spiritstudios.specter.api.config.ModMenuHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.GrassColors;

public class HollowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(HollowEntityTypes.FIREFLY, FireflyEntityRenderer::new);

        ModelPredicateProviderRegistry.register(
                HollowItems.COPPER_HORN,
                Identifier.ofVanilla("tooting"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );

        BlockEntityRendererFactories.register(HollowBlockEntityTypes.JAR_BLOCK_ENTITY, JarBlockEntityRenderer::new);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? 0x208030 : 0x71C35C, HollowBlocks.LOTUS_LILYPAD);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x71C35C : -1, HollowItems.LOTUS_LILYPAD);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? 0x208030 : 0x71C35C, HollowBlocks.GIANT_LILYPAD);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x71C35C : -1, HollowItems.GIANT_LILYPAD);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        tintIndex != 0 ?
                                world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor() :
                                -1,
                HollowBlocks.BLUE_WILDFLOWER,
                HollowBlocks.WHITE_WILDFLOWER,
                HollowBlocks.PURPLE_WILDFLOWER,
                HollowBlocks.PINK_WILDFLOWER
        );

        ParticleFactoryRegistry.getInstance().register(HollowParticleTypes.FIREFLY_JAR, FireflyJarParticle.Factory::new);

        ModMenuHelper.addConfig(Hollow.MODID, HollowConfig.HOLDER.id());

        BlockEntityRendererFactories.register(
                HollowBlockEntityTypes.ECHOING_POT_BLOCK_ENTITY,
                EchoingPotBlockEntityRenderer::new
        );
    }
}