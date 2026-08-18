package com.keko.stellaria.entity.boss.minions.zregos.client.ZregosServant;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosServantModel extends GeoModel<ZregosServant> {
    @Override
    public ResourceLocation getModelResource(ZregosServant zregosServant) {
        return Stellaria.id("geo/zregos_servant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosServant zregosServant) {
        return Stellaria.id("textures/entity/zregos_servant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosServant zregosServant) {
        return Stellaria.id("animations/zregos_servant.animation.json");
    }
}
