package com.keko.stellaria.entity.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.einfarbig.EinfarbigStatue;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EinfarbigStatueModel extends GeoModel<EinfarbigStatue> {
    @Override
    public ResourceLocation getModelResource(EinfarbigStatue animatable) {
        return Stellaria.id("geo/einfarbig_statue.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EinfarbigStatue animatable) {
        return Stellaria.id("textures/entity/einfarbig_statue.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EinfarbigStatue animatable) {
        return Stellaria.id("animations/einfarbig_statue.animation.json");
    }
}
