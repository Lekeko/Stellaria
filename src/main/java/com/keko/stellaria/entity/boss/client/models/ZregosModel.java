package com.keko.stellaria.entity.boss.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosModel extends GeoModel<Zregos> {
    @Override
    public ResourceLocation getModelResource(Zregos zregos) {
        return Stellaria.id("geo/zregos_head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Zregos zregos) {
        return Stellaria.id("textures/entity/zregos_head.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Zregos zregos) {
        return Stellaria.id("animations/zregos_head.animation.json");
    }
}
