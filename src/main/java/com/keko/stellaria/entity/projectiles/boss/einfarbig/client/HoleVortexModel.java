package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.HoleVortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HoleVortexModel extends GeoModel<HoleVortex> {
    @Override
    public ResourceLocation getModelResource(HoleVortex animatable) {
        return Stellaria.id("geo/bunny_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HoleVortex animatable) {
        return Stellaria.id("textures/normal_vfx/hole_vortex.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HoleVortex animatable) {
        return Stellaria.id("animations/bunny_hat.animation.json");
    }
}
