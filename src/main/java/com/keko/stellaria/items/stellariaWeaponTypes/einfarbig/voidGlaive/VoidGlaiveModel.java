package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.voidGlaive;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidGlaiveModel extends GeoModel<VoidGlaive> {
    @Override
    public ResourceLocation getModelResource(VoidGlaive animatable) {
        return Stellaria.id("geo/void_glaive.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VoidGlaive animatable) {
        return Stellaria.id("textures/item/void_glaive.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidGlaive animatable) {
        return null;
    }
}
