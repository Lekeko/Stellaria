package com.keko.stellaria.entity.projectiles.player.client.explosive.CosmicShieldProjectile;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.explosive.CosmicShieldProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CosmicShieldProjectileModel extends GeoModel<CosmicShieldProjectile> {
    @Override
    public ResourceLocation getModelResource(CosmicShieldProjectile animatable) {
        return Stellaria.id("geo/cosmic_shield_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CosmicShieldProjectile animatable) {
        return Stellaria.id("textures/entity/cosmic_shield_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CosmicShieldProjectile animatable) {
        return null;

    }
}
