package com.keko.stellaria.entity.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EinfarbigModel extends GeoModel<Einfarbig> {
    @Override
    public ResourceLocation getModelResource(Einfarbig animatable) {
        return Stellaria.id("geo/einfarbig.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Einfarbig animatable) {
        return Stellaria.id("textures/entity/einfarbig.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Einfarbig animatable) {
        return Stellaria.id("animations/einfarbig.animation.json");
    }
}
