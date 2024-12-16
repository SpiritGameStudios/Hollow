package dev.spiritstudios.hollow.registry;

import dev.spiritstudios.hollow.worldgen.foliage.BlobWithHangingFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public final class HollowFoliagePlacerTypes {
    public static final FoliagePlacerType<BlobWithHangingFoliagePlacer> BLOB_WITH_HANGING = new FoliagePlacerType<>(BlobWithHangingFoliagePlacer.CODEC);
}
