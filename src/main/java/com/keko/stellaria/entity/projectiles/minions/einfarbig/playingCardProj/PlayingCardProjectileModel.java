package com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.ZregosBody;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PlayingCardProjectileModel extends GeoModel<PlayingCardProjectile> {
    @Override
    public ResourceLocation getModelResource(PlayingCardProjectile playingCardProjectile) {
        return Stellaria.id("geo/playing_card_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlayingCardProjectile playingCardProjectile) {
        return Stellaria.id("textures/entity/playing_card_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlayingCardProjectile playingCardProjectile) {
        return null;
    }
}
