package com.keko.stellaria.entity.decour.client.zregosMace;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.decour.zregosBoulders.ZregosBoulderNormal;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosMaceModel extends GeoModel<ZregosMace> {
    @Override
    public ResourceLocation getModelResource(ZregosMace animatable) {
        return Stellaria.id("geo/zregos_mace.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosMace animatable) {
        return Stellaria.id("textures/entity/zregos_mace.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosMace animatable) {
        return Stellaria.id("animations/zregos_mace.animation.json");
    }
}
