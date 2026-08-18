package com.keko.stellaria.entity.decour.zregosBoulders.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosBoulderNormalModel extends GeoModel<ZregosBoulderNormal> {
    @Override
    public ResourceLocation getModelResource(ZregosBoulderNormal animatable) {
        return Stellaria.id("geo/zregos_boulders_normal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosBoulderNormal animatable) {
        return Stellaria.id("textures/entity/zregos_boulders.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosBoulderNormal animatable) {
        return Stellaria.id("animations/zregos_boulders_normal.animation.json");
    }
}
