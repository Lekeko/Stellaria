package com.keko.stellaria.entity.boss.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.ZregosBody;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosBodyModel extends GeoModel<ZregosBody> {
    @Override
    public ResourceLocation getModelResource(ZregosBody zregos) {
        return Stellaria.id("geo/zregos_body.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosBody zregos) {
        return Stellaria.id("textures/entity/zregos_body.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosBody zregos) {
        return null;
    }
}
