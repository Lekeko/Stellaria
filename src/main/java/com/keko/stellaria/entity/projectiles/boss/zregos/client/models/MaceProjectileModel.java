package com.keko.stellaria.entity.projectiles.boss.zregos.client.models;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.MaceProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MaceProjectileModel extends GeoModel<MaceProjectile> {
    @Override
    public ResourceLocation getModelResource(MaceProjectile animatable) {
        return Stellaria.id("geo/mace_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MaceProjectile animatable) {
        return Stellaria.id("textures/entity/mace_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MaceProjectile animatable) {
        return null;
    }
}
