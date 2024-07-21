package dev.callmeecho.hollow.mixin;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.callmeecho.hollow.main.Hollow.MODID;

@Mixin(SaplingGenerator.class)
public class SaplingGeneratorMixin {
    @Inject(method = "getSmallTreeFeature", at = @At("HEAD"), cancellable = true)
    protected void getSmallTreeFeature(Random random, boolean bees, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        cir.setReturnValue(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MODID, "birch_bees_0002")));
    }

    @Inject(method = "getMegaTreeFeature", at = @At("HEAD"), cancellable = true)
    protected void getMegaTreeFeature(Random random, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        cir.setReturnValue(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MODID, "birch_tall")));
    }
}
