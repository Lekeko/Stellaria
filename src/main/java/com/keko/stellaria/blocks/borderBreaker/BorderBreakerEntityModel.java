package com.keko.stellaria.blocks.borderBreaker;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.portal.DraconicCrystalBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BorderBreakerEntityModel extends GeoModel<BorderBreakerEntity> {
    @Override
    public ResourceLocation getModelResource(BorderBreakerEntity animatable) {
        return Stellaria.id("geo/border_breaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BorderBreakerEntity animatable) {
        return Stellaria.id("textures/block/border_breaker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BorderBreakerEntity animatable) {
        return Stellaria.id("animations/border_breaker.animation.json");
    }
}
