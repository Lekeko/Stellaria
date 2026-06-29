package com.keko.stellaria.entity.projectiles.player.client.mage.amethystProjectile;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.AmethystProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AmethystProjectileModel extends GeoModel<AmethystProjectile> {
    @Override
    public ResourceLocation getModelResource(AmethystProjectile animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(AmethystProjectile animatable) {
        return Stellaria.id("textures/entity/amethyst_projectile.png");

    }

    @Override
    public ResourceLocation getAnimationResource(AmethystProjectile animatable) {
        return null;
    }
}
