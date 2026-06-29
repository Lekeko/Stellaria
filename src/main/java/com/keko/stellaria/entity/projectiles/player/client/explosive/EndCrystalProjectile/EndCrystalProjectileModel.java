package com.keko.stellaria.entity.projectiles.player.client.explosive.EndCrystalProjectile;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EndCrystalProjectileModel extends GeoModel<EndCrystalProjectile> {
    @Override
    public ResourceLocation getModelResource(EndCrystalProjectile animatable) {
        return Stellaria.id("geo/end_crystal_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EndCrystalProjectile animatable) {
        return Stellaria.id("textures/entity/end_crystal_projectile.png");

    }

    @Override
    public ResourceLocation getAnimationResource(EndCrystalProjectile animatable) {
        return Stellaria.id("animations/end_crystal_projectile.animation.json");
    }
}
