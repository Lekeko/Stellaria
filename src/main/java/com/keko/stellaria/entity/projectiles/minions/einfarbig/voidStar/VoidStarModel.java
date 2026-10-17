package com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VoidStarModel extends GeoModel<VoidStar> {
    @Override
    public ResourceLocation getModelResource(VoidStar voidStar) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(VoidStar voidStar) {
        return Stellaria.id("textures/entity/star_burst_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VoidStar voidStar) {
        return null;
    }
}
