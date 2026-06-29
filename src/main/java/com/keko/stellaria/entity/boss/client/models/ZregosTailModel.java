package com.keko.stellaria.entity.boss.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.ZregosTail;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosTailModel extends GeoModel<ZregosTail> {
    @Override
    public ResourceLocation getModelResource(ZregosTail zregos) {
        return Stellaria.id("geo/zregos_tail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosTail zregos) {
        return Stellaria.id("textures/entity/zregos_tail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosTail zregos) {
        return null;
    }
}
