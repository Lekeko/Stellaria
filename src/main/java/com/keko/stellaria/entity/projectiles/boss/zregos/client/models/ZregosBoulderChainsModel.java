package com.keko.stellaria.entity.projectiles.boss.zregos.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosBouldersChains;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosBoulderChainsModel extends GeoModel<ZregosBouldersChains> {
    @Override
    public ResourceLocation getModelResource(ZregosBouldersChains animatable) {
        return Stellaria.id("geo/zregos_boulders_chains.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosBouldersChains animatable) {
        return Stellaria.id("textures/entity/zregos_boulders_chains.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosBouldersChains animatable) {
        return Stellaria.id("animations/zregos_boulders_chains.animation.json");
    }
}
