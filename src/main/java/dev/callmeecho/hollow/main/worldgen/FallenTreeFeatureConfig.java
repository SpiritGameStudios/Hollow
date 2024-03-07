package dev.callmeecho.hollow.main.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class FallenTreeFeatureConfig implements FeatureConfig {
    public static final Codec<FallenTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider)
    ).apply(instance, FallenTreeFeatureConfig::new));
    
    public final BlockStateProvider stateProvider;
    
    public FallenTreeFeatureConfig(BlockStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }
}
