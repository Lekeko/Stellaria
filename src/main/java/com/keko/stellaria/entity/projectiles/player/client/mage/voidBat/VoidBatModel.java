package com.keko.stellaria.entity.projectiles.player.client.mage.voidBat;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.VoidBat;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidBatModel extends GeoModel<VoidBat> {
    @Override
    public ResourceLocation getModelResource(VoidBat animatable) {
        return Stellaria.id("geo/void_bat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidBat animatable) {
        return Stellaria.id("textures/entity/void_bat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidBat animatable) {
        return Stellaria.id("animations/void_bat.animation.json");

    }
}
