package com.keko.stellaria.blocks.voidLamp;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.borderBreaker.BorderBreakerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidLampEntityModel extends GeoModel<VoidLampEntity> {
    @Override
    public ResourceLocation getModelResource(VoidLampEntity animatable) {
        return Stellaria.id("geo/void_lamp.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidLampEntity animatable) {
        return Stellaria.id("textures/block/void_lamp.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidLampEntity animatable) {
        return null;
    }
}
