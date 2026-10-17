package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.trickstershat;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.whiteoutStaff.WhiteOutStaff;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TrickstersHatModel extends GeoModel<TrickstersHat> {
    @Override
    public ResourceLocation getModelResource(TrickstersHat animatable) {
        return Stellaria.id("geo/tricksters_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TrickstersHat animatable) {
        return Stellaria.id("textures/item/tricksters_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TrickstersHat animatable) {
        return Stellaria.id("animations/tricksters_hat.animation.json");
    }
}
