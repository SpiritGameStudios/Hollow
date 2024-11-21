package dev.spiritstudios.hollow.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record FallenTreeFeatureConfig(BlockStateProvider stateProvider, boolean polypore, boolean mossy) implements FeatureConfig {
    public static final Codec<FallenTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider),
            Codec.BOOL.fieldOf("polypore").orElse(false).forGetter(config -> config.polypore),
            Codec.BOOL.fieldOf("mossy").orElse(false).forGetter(config -> config.mossy)
    ).apply(instance, FallenTreeFeatureConfig::new));
}
