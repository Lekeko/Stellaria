package com.keko.stellaria.entity.projectiles.player.client.melee;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.entity.projectiles.player.melee.SerpentsProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SerpentsProjectileModel extends GeoModel<SerpentsProjectile> {
    @Override
    public ResourceLocation getModelResource(SerpentsProjectile animatable) {
        return Stellaria.id("geo/serpents_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SerpentsProjectile animatable) {
        return Stellaria.id("textures/entity/serpents_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SerpentsProjectile animatable) {
        return Stellaria.id("animations/serpents_projectile.animation.json");

    }
}
