package com.keko.stellaria.entity.decour.zregosBoulders.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderShattered;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosBoulderShatteredModel extends GeoModel<ZregosBoulderShattered> {
    @Override
    public ResourceLocation getModelResource(ZregosBoulderShattered animatable) {
        return Stellaria.id("geo/zregos_boulders.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosBoulderShattered animatable) {
        return Stellaria.id("textures/entity/zregos_boulders.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosBoulderShattered animatable) {
        return Stellaria.id("animations/zregos_boulders.animation.json");
    }
}
