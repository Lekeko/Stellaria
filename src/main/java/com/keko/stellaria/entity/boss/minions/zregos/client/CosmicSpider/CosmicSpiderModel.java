package com.keko.stellaria.entity.boss.minions.zregos.client.CosmicSpider;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.zregos.CosmicSpider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CosmicSpiderModel extends GeoModel<CosmicSpider> {
    @Override
    public ResourceLocation getModelResource(CosmicSpider cosmicSpider) {
        return Stellaria.id("geo/cosmic_spider.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CosmicSpider cosmicSpider) {
        return Stellaria.id("textures/entity/cosmic_spider.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CosmicSpider cosmicSpider) {
        return Stellaria.id("animations/cosmic_spider.animation.json");
    }
}
