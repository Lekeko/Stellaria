package com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CosmicBowModel extends GeoModel<CosmicBow> {
    @Override
    public ResourceLocation getModelResource(CosmicBow animatable) {
        return Stellaria.id("geo/cosmic_bow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CosmicBow animatable) {
        return Stellaria.id("textures/item/cosmic_bow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CosmicBow animatable) {
        return Stellaria.id("animations/cosmic_bow.animation.json");
    }
}
