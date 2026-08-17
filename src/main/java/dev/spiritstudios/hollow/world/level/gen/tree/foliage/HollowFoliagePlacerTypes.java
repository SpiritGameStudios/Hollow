package dev.spiritstudios.hollow.world.level.gen.tree.foliage;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.hollow.Hollow;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public final class HollowFoliagePlacerTypes {
    public static final FoliagePlacerType<BlobWithHangingFoliagePlacer> BLOB_WITH_HANGING = register("blob_with_hanging_foliage_placer", BlobWithHangingFoliagePlacer.CODEC);

    private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, Hollow.id(name), new FoliagePlacerType<>(codec));
    }

    public static void init() {
        // NO-OP
    }
}
