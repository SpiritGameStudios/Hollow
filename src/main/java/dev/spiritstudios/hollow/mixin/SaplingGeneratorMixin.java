package dev.spiritstudios.hollow.mixin;

import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.spiritstudios.hollow.Hollow.MODID;

@Mixin(SaplingGenerator.class)
public class SaplingGeneratorMixin {
    @Shadow @Final private String id;

    @Inject(method = "getSmallTreeFeature", at = @At("HEAD"), cancellable = true)
    protected void getSmallTreeFeature(Random random, boolean bees, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        if (!this.id.equals("birch")) return;
        cir.setReturnValue(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MODID, "birch_bees_0002")));
    }

    @Inject(method = "getMegaTreeFeature", at = @At("HEAD"), cancellable = true)
    protected void getMegaTreeFeature(Random random, CallbackInfoReturnable<RegistryKey<ConfiguredFeature<?, ?>>> cir) {
        if (!this.id.equals("birch")) return;
        cir.setReturnValue(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MODID, "birch_tall")));
    }
}
