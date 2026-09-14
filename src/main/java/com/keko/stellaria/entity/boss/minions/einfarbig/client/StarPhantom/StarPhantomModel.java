package com.keko.stellaria.entity.boss.minions.einfarbig.client.StarPhantom;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.zregos.CosmicSpider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StarPhantomModel extends GeoModel<StarPhantom> {
    @Override
    public ResourceLocation getModelResource(StarPhantom starPhantom) {
        return Stellaria.id("geo/star_phantom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StarPhantom starPhantom) {
        return Stellaria.id("textures/entity/star_phantom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StarPhantom starPhantom) {
        return Stellaria.id("animations/star_phantom.animation.json");
    }
}
