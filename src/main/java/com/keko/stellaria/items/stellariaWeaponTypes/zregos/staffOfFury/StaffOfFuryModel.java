package com.keko.stellaria.items.stellariaWeaponTypes.zregos.staffOfFury;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.items.stellariaWeaponTypes.zregos.cosmicBow.CosmicBow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StaffOfFuryModel extends GeoModel<StaffOfFury> {
    @Override
    public ResourceLocation getModelResource(StaffOfFury animatable) {
        return Stellaria.id("geo/staff_of_fury.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StaffOfFury animatable) {
        return Stellaria.id("textures/item/staff_of_fury.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StaffOfFury animatable) {
        return Stellaria.id("animations/staff_of_fury.animation.json");
    }
}
