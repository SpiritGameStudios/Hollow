package dev.spiritstudios.hollow.worldgen.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.hollow.registry.HollowFoliagePlacerRegistrar;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class BlobWithHangingFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<BlobWithHangingFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            fillFoliagePlacerFields(instance).and(instance.group(
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
    protected FoliagePlacerType<?> getType() {
        return HollowFoliagePlacerRegistrar.BLOB_WITH_HANGING;
    }

    @Override
    protected void generate(
            TestableWorld world,
            FoliagePlacer.BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            FoliagePlacer.TreeNode treeNode,
            int foliageHeight,
            int radius,
            int offset
    ) {
        BlockPos adjustedPos = treeNode.getCenter().up(offset);

        for (int i = offset - foliageHeight; i <= offset; i++) {
            this.generateSquareWithHangingLeaves(
                    world, placer, random, config,
                    adjustedPos,
                    Math.max(radius + treeNode.getFoliageRadius() - 1 - i / 2, 0), i,
                    treeNode.isGiantTrunk(),
                    hangingLeavesChance, hangingLeavesExtensionChance
            );
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return this.height;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx == radius && dz == radius && (random.nextInt(2) == 0 || y == 0);
    }
}
