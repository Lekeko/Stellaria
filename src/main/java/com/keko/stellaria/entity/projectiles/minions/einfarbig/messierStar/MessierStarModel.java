package com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MessierStarModel extends GeoModel<MessierStar> {
    @Override
    public ResourceLocation getModelResource(MessierStar messierStar) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(MessierStar messierStar) {
        return Stellaria.id("textures/entity/star_burst_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MessierStar playingCardProjectile) {
        return null;
    }
}