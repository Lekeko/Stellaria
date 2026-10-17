package com.keko.stellaria.items.stellariaWeaponTypes.einfarbig.whiteoutStaff;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WhiteOutStaffModel extends GeoModel<WhiteOutStaff> {
    @Override
    public ResourceLocation getModelResource(WhiteOutStaff animatable) {
        return Stellaria.id("geo/whiteout_staff.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WhiteOutStaff animatable) {
        return Stellaria.id("textures/item/whiteout_staff.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WhiteOutStaff animatable) {
        return Stellaria.id("animations/whiteout_staff.animation.json");
    }
}
