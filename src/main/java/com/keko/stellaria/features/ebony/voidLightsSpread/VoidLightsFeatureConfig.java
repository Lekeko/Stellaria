package com.keko.stellaria.features.ebony.voidLightsSpread;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VoidLightsFeatureConfig (int number, ResourceLocation blockId) implements FeatureConfiguration {
    public static final Codec<VoidLightsFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ExtraCodecs.POSITIVE_INT.fieldOf("number").forGetter(VoidLightsFeatureConfig::number),
                            ResourceLocation.CODEC.fieldOf("blockID").forGetter(VoidLightsFeatureConfig::blockId))
                    .apply(instance, VoidLightsFeatureConfig::new));
}
