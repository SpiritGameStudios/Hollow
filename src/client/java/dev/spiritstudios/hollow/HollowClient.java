package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.particle.FireflyJarParticle;
import dev.spiritstudios.hollow.particle.ScreamParticle;
import dev.spiritstudios.hollow.registry.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.registry.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import dev.spiritstudios.hollow.registry.HollowItems;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.hollow.render.block.EchoingPotBlockEntityRenderer;
import dev.spiritstudios.hollow.render.block.EchoingVaseBlockEntityRenderer;
import dev.spiritstudios.hollow.render.block.JarBlockEntityRenderer;
import dev.spiritstudios.hollow.render.entity.FireflyEntityRenderer;
import dev.spiritstudios.specter.api.config.ModMenuHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

public class HollowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMenuHelper.addConfig(Hollow.MODID, HollowConfig.HOLDER.id());

        EntityRendererRegistry.register(HollowEntityTypes.FIREFLY, FireflyEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(
                HollowParticleTypes.FIREFLY_JAR,
                FireflyJarParticle.Factory::new
        );

        ParticleFactoryRegistry.getInstance().register(
                HollowParticleTypes.SCREAM,
                ScreamParticle.Factory::new
        );

        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null ? 0x208030 : 0x71C35C,
                HollowBlocks.GIANT_LILYPAD, HollowBlocks.LOTUS_LILYPAD
        );

        ModelPredicateProviderRegistry.register(
                HollowItems.COPPER_HORN,
                Identifier.ofVanilla("tooting"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );

        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> tintIndex == 0 ? 0x71C35C : -1,
                HollowItems.GIANT_LILYPAD, HollowItems.LOTUS_LILYPAD
        );

        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) ->
                        world != null && pos != null ? 0x208030 : 0x71C35C,
                HollowBlocks.GIANT_LILYPAD, HollowBlocks.LOTUS_LILYPAD
        );

        // region Block Entity Renderers
        BlockEntityRendererFactories.register(
                HollowBlockEntityTypes.JAR,
                JarBlockEntityRenderer::new
        );

        BlockEntityRendererFactories.register(
                HollowBlockEntityTypes.ECHOING_POT,
                EchoingPotBlockEntityRenderer::new
        );

        BlockEntityRendererFactories.register(
                HollowBlockEntityTypes.ECHOING_VASE,
                EchoingVaseBlockEntityRenderer::new
        );
        // endregion
    }
}