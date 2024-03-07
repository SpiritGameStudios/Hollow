package dev.callmeecho.hollow.mixin;

import net.minecraft.block.sapling.BirchSaplingGenerator;
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

@Mixin(BirchSaplingGenerator.class)
public class BirchSaplingGeneratorMixin {
    @Inject(method = "getTreeFeature", at = @At("HEAD"), cancellable = true)
    protected void getTreeFeature(Random random, boolean bees, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        cir.setReturnValue(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(MODID, "birch_bees_0002")));
    }
}
