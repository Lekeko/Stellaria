package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.einfarbig.Einfarbig;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BunnyHatModel extends GeoModel<BunnyHat> {
    @Override
    public ResourceLocation getModelResource(BunnyHat animatable) {
        return Stellaria.id("geo/bunny_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BunnyHat animatable) {
        return Stellaria.id("textures/entity/bunny_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BunnyHat animatable) {
        return Stellaria.id("animations/bunny_hat.animation.json");
    }
}
