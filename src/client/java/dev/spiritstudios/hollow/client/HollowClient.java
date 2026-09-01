package dev.spiritstudios.hollow.client;

import dev.spiritstudios.hollow.Hollow;
import dev.spiritstudios.hollow.client.color.block.HollowBlockTintSources;
import dev.spiritstudios.hollow.client.color.item.Jeb;
import dev.spiritstudios.hollow.client.event.ClientPacketListenerEvents;
import dev.spiritstudios.hollow.client.model.geom.HollowModelLayers;
import dev.spiritstudios.hollow.client.render.entity.FurnaceBoatRenderer;
import dev.spiritstudios.hollow.client.render.particle.JarFireflyParticle;
import dev.spiritstudios.hollow.client.render.particle.ScreamParticle;
import dev.spiritstudios.hollow.client.sound.MovingEntitySoundInstance;
import dev.spiritstudios.hollow.client.sound.MovingEntitySoundInstanceBuilder;
import dev.spiritstudios.hollow.sounds.HollowSoundEvents;
import dev.spiritstudios.hollow.world.entity.HollowEntityTypes;
import dev.spiritstudios.hollow.world.entity.vehicle.AbstractFurnaceBoat;
import dev.spiritstudios.hollow.world.level.block.entity.HollowBlockEntityTypes;
import dev.spiritstudios.hollow.world.level.block.HollowBlocks;
import dev.spiritstudios.hollow.core.particles.HollowParticleTypes;
import dev.spiritstudios.hollow.client.render.block.pot.PotRenderer;
import dev.spiritstudios.hollow.client.render.block.pot.FallingPotRenderer;
import dev.spiritstudios.hollow.client.render.block.GlassJarBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class HollowClient implements ClientModInitializer {
	private static final MovingEntitySoundInstanceBuilder<AbstractFurnaceBoat> FURNACE_BOAT_LOOPER = MovingEntitySoundInstance.<AbstractFurnaceBoat>builder()
		.soundEvent(HollowSoundEvents.FURNACE_BOAT_LOOP)
		.soundSource(SoundSource.NEUTRAL)
		.movingPredicate((_, furnaceBoat) -> furnaceBoat.hasFuel())
		.pitchRange(1.0F, 1.0F)
		.volume(1.0F);

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(HollowParticleTypes.SCREAM, ScreamParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(HollowParticleTypes.JAR_FIREFLY, JarFireflyParticle.Provider::new);

		ClientPacketListenerEvents.ADD_ENTITY_SOUND_INSTANCE.register((_, soundManager, entity) -> {
			if (entity instanceof AbstractFurnaceBoat furnaceBoat)
				soundManager.queueTickingSound(FURNACE_BOAT_LOOPER.buildAndApplyTo(furnaceBoat));
		});

        BlockColorRegistry.register(
                List.of(BlockTintSources.constant(BlockColors.LILY_PAD_DEFAULT, BlockColors.LILY_PAD_IN_WORLD)),
                HollowBlocks.GIANT_LILY_PAD, HollowBlocks.FLOWERING_LILY_PAD
        );
		BlockColorRegistry.register(
			List.of(HollowBlockTintSources.cattail()),
			HollowBlocks.CATTAIL
		);

		ItemTintSources.ID_MAPPER.put(Hollow.id("jeb"), Jeb.MAP_CODEC);

        // region Block Entity Renderers
        BlockEntityRenderers.register(HollowBlockEntityTypes.GLASS_JAR, GlassJarBlockEntityRenderer::new);

        BlockEntityRenderers.register(HollowBlockEntityTypes.POT, PotRenderer::new);
        BlockEntityRenderers.register(HollowBlockEntityTypes.FALLING_POT, FallingPotRenderer::new);
		BlockEntityRenderers.register(HollowBlockEntityTypes.OBABO, PotRenderer::new);
		// endregion

		HollowModelLayers.init();

		EntityRenderers.register(HollowEntityTypes.ACACIA_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.ACACIA_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.BAMBOO_FURNACE_RAFT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.BAMBOO_FURNACE_RAFT));
		EntityRenderers.register(HollowEntityTypes.BIRCH_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.BIRCH_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.CHERRY_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.CHERRY_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.DARK_OAK_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.DARK_OAK_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.JUNGLE_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.JUNGLE_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.MANGROVE_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.MANGROVE_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.OAK_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.OAK_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.PALE_OAK_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.PALE_OAK_FURNACE_BOAT));
		EntityRenderers.register(HollowEntityTypes.SPRUCE_FURNACE_BOAT, context -> new FurnaceBoatRenderer(context, HollowModelLayers.SPRUCE_FURNACE_BOAT));
    }
}
