package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.worldgen.foliage.BlobWithHangingFoliagePlacer;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public final class HollowFoliagePlacerTypes implements MinecraftRegistrar<FoliagePlacerType<?>> {
    public static final FoliagePlacerType<BlobWithHangingFoliagePlacer> BLOB_WITH_HANGING = new FoliagePlacerType<>(BlobWithHangingFoliagePlacer.CODEC);

    @Override
    public Registry<FoliagePlacerType<?>> getRegistry() {
        return Registries.FOLIAGE_PLACER_TYPE;
    }

    @Override
    public Class<FoliagePlacerType<?>> getObjectType() {
        return Registrar.fixGenerics(FoliagePlacerType.class);
    }
}
