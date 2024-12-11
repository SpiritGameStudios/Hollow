package dev.spiritstudios.hollow.datagen.tag;

import dev.spiritstudios.hollow.registry.HollowEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
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
