package com.keko.stellaria.entity.projectiles.player.client.mage.FuryProjectile;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FuryProjectileModel extends GeoModel<FuryProjectile> {
    @Override
    public ResourceLocation getModelResource(FuryProjectile animatable) {
        return Stellaria.id("geo/fury_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FuryProjectile animatable) {
        return Stellaria.id("textures/entity/fury_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FuryProjectile animatable) {
        return Stellaria.id("animations/fury_projectile.animation.json");

    }
}
