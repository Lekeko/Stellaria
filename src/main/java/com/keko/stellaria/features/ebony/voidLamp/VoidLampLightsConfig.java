package com.keko.stellaria.features.ebony.voidLamp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VoidLampLightsConfig(int number, ResourceLocation blockId) implements FeatureConfiguration {
    public static final Codec<VoidLampLightsConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ExtraCodecs.POSITIVE_INT.fieldOf("number").forGetter(VoidLampLightsConfig::number),
                            ResourceLocation.CODEC.fieldOf("blockID").forGetter(VoidLampLightsConfig::blockId))
                    .apply(instance, VoidLampLightsConfig::new));
}
