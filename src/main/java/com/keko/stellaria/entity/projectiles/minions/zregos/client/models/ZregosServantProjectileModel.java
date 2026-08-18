package com.keko.stellaria.entity.projectiles.minions.zregos.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosServantProjectileModel extends GeoModel<ZregosServantProjectile> {
    @Override
    public ResourceLocation getModelResource(ZregosServantProjectile animatable) {
        return Stellaria.id("geo/zregos_servant_projectile.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(ZregosServantProjectile animatable) {
        return Stellaria.id("textures/entity/zregos_servant_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosServantProjectile animatable) {
        return null;

    }
}
