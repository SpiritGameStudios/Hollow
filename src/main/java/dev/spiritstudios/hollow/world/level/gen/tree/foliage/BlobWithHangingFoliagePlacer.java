package dev.spiritstudios.hollow.world.level.gen.tree.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class BlobWithHangingFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<BlobWithHangingFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            foliagePlacerParts(instance).and(instance.group(
                            Codec.intRange(0, 16).fieldOf("height").forGetter(placer -> placer.height),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(placer -> placer.hangingLeavesChance),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(placer -> placer.hangingLeavesExtensionChance)
                    )
            ).apply(instance, BlobWithHangingFoliagePlacer::new));

    protected final int height;
    protected final float hangingLeavesChance;
    protected final float hangingLeavesExtensionChance;

    public BlobWithHangingFoliagePlacer(IntProvider radius, IntProvider offset, int height, float hangingLeavesChance, float hangingLeavesExtensionChance) {
        super(radius, offset);
        this.height = height;
        this.hangingLeavesChance = hangingLeavesChance;
        this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return HollowFoliagePlacerTypes.BLOB_WITH_HANGING;
    }

    @Override
    protected void createFoliage(WorldGenLevel level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius, int offset) {
        BlockPos adjustedPos = foliageAttachment.pos().above(offset);

        for (int i = offset - foliageHeight; i <= offset; i++) {
            this.placeLeavesRowWithHangingLeavesBelow(
                    level, foliageSetter, random, config,
                    adjustedPos,
                    Math.max(leafRadius + foliageAttachment.radiusOffset() - 1 - i / 2, 0), i,
                    foliageAttachment.doubleTrunk(),
                    hangingLeavesChance, hangingLeavesExtensionChance
            );
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx == radius && dz == radius && (random.nextInt(2) == 0 || y == 0);
    }
}
