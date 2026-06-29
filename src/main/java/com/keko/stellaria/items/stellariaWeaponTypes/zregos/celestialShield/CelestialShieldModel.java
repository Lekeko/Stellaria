package com.keko.stellaria.items.stellariaWeaponTypes.zregos.celestialShield;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow.CosmicBow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CelestialShieldModel extends GeoModel<CelestialShield> {
    @Override
    public ResourceLocation getModelResource(CelestialShield animatable) {
        return Stellaria.id("geo/celestial_shield.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CelestialShield animatable) {
        return Stellaria.id("textures/item/celestial_shield.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CelestialShield animatable) {
        return Stellaria.id("animations/celestial_shield.animation.json");
    }
}
