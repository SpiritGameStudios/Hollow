package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.data.worldgen.features.TreeFeatures;
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
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getSmallTreeFeature(RandomSource random, boolean hasFlowers, Operation<ResourceKey<ConfiguredFeature<?, ?>>> original) {
        return this.name.equals("birch") ? TreeFeatures.BIRCH_BEES_0002 : original.call(random, hasFlowers);
    }
}
