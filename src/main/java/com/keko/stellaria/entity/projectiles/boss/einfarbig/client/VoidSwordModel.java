package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.VoidSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidSwordModel extends GeoModel<VoidSword> {
    @Override
    public ResourceLocation getModelResource(VoidSword animatable) {
        return Stellaria.id("geo/void_sword.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidSword animatable) {
        return Stellaria.id("textures/entity/void_sword.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidSword animatable) {
        return Stellaria.id("animations/void_sword.animation.json");
    }
}
