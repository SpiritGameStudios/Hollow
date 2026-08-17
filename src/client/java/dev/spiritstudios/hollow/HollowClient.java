package dev.spiritstudios.hollow;

import dev.spiritstudios.hollow.render.particle.JarFireflyParticle;
import dev.spiritstudios.hollow.render.particle.ScreamParticle;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.registry.HollowParticleTypes;
import dev.spiritstudios.hollow.render.block.EchoingPotBlockEntityRenderer;
import dev.spiritstudios.hollow.render.block.EchoingVaseBlockEntityRenderer;
import dev.spiritstudios.hollow.render.block.JarBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import java.util.List;

public class HollowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(HollowParticleTypes.SCREAM, ScreamParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(HollowParticleTypes.JAR_FIREFLY, JarFireflyParticle.Provider::new);

        BlockColorRegistry.register(
                List.of(BlockTintSources.constant(BlockColors.LILY_PAD_DEFAULT, BlockColors.LILY_PAD_IN_WORLD)),
                HollowBlocks.GIANT_LILY_PAD, HollowBlocks.FLOWERING_LILY_PAD
        );

        // region Block Entity Renderers
        BlockEntityRenderers.register(
                HollowBlockEntityTypes.JAR,
                JarBlockEntityRenderer::new
        );

        BlockEntityRenderers.register(
                HollowBlockEntityTypes.ECHOING_POT,
                EchoingPotBlockEntityRenderer::new
        );

        BlockEntityRenderers.register(
                HollowBlockEntityTypes.ECHOING_VASE,
                EchoingVaseBlockEntityRenderer::new
        );
        // endregion
    }
}