package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TreeGrower.class)
public abstract class TreeGrowerMixin {
    @Shadow
    @Final
    private String name;

    @WrapMethod(method = "getConfiguredFeature")
    protected @Nullable ResourceKey<Feature> getSmallTreeFeature(RandomSource random, boolean hasFlowers, Operation<ResourceKey<Feature>> original) {
        return this.name.equals("birch") ? TreeFeatures.BIRCH_BEES_0002 : original.call(random, hasFlowers);
    }
}
