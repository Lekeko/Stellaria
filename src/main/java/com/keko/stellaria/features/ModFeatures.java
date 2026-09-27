package com.keko.stellaria.features;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.features.ebony.voidLightsSpread.VoidLightsFeature;
import com.keko.stellaria.features.ebony.voidLightsSpread.VoidLightsFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModFeatures {
    public static final ResourceLocation VOID_LIGHTS_SPREAD_FEATURE_ID = Stellaria.id("void_lightstone_spread_feature");
    public static final VoidLightsFeature VOID_LIGHTS_SPREAD_FEATURE = new VoidLightsFeature(VoidLightsFeatureConfig.CODEC);

    public static void init(){
//        Registry.register(BuiltInRegistries.FEATURE, VOID_LIGHTS_SPREAD_FEATURE_ID, VOID_LIGHTS_SPREAD_FEATURE);
    }
}
