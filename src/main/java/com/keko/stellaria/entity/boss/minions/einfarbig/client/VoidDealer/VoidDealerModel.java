package com.keko.stellaria.entity.boss.minions.einfarbig.client.VoidDealer;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidDealerModel extends GeoModel<VoidDealer> {
    @Override
    public ResourceLocation getModelResource(VoidDealer voidDealer) {
        return Stellaria.id("geo/void_dealer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidDealer voidDealer) {
        return Stellaria.id("textures/entity/void_dealer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidDealer voidDealer) {
        return Stellaria.id("animations/void_dealer.animation.json");
    }
}
