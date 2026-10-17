package com.keko.stellaria.features;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.features.ebony.voidLamp.VoidLampLightsFeature;
import com.keko.stellaria.features.ebony.voidLamp.VoidLampLightsConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModFeatures {
    public static final ResourceLocation VOID_LAMP_LIGHTS_FEATURE_ID = Stellaria.id("void_lamp_lights_feature");
    public static final VoidLampLightsFeature VOID_LAMP_LIGHTS_FEATURE = new VoidLampLightsFeature(VoidLampLightsConfig.CODEC);

    public static void init(){
        Registry.register(BuiltInRegistries.FEATURE, VOID_LAMP_LIGHTS_FEATURE_ID, VOID_LAMP_LIGHTS_FEATURE);
    }
}
