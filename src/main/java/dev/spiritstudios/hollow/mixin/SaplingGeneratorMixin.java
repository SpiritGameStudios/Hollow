package dev.spiritstudios.hollow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SaplingGenerator.class)
public abstract class SaplingGeneratorMixin {
    @Shadow
    @Final
    private String id;

    @WrapMethod(method = "getSmallTreeFeature")
    protected @Nullable RegistryKey<ConfiguredFeature<?, ?>> getSmallTreeFeature(Random random, boolean flowersNearby, Operation<RegistryKey<ConfiguredFeature<?, ?>>> original) {
        return !this.id.equals("birch") ?
                original.call(random, flowersNearby) :
                TreeConfiguredFeatures.BIRCH_BEES_0002;
    }
}
