package com.keko.stellaria.entity.projectiles.boss.client.models.zregos;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosProjectieModel extends GeoModel<ZregosProjectile> {
    @Override
    public ResourceLocation getModelResource(ZregosProjectile zregosProjectile) {
        return Stellaria.id("geo/zregos_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosProjectile zregosProjectile) {
        return Stellaria.id("textures/entity/zregos_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosProjectile zregosProjectile) {
        return null;
    }
}
