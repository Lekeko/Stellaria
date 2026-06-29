package com.keko.stellaria.items.stellariaWeaponTypes.zregos.serpentsBlade;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SerpentsBladeModel extends GeoModel<SerpentsBlade> {
    @Override
    public ResourceLocation getModelResource(SerpentsBlade animatable) {
        return Stellaria.id("geo/serpents_blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SerpentsBlade animatable) {
        return Stellaria.id("textures/item/serpents_blade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SerpentsBlade animatable) {
        return null;
    }
}
