package com.keko.stellaria.entity.boss.minions.zregos.client.Crystalius;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CrystaliusModel extends GeoModel<Crystalius> {
    @Override
    public ResourceLocation getModelResource(Crystalius crystalius) {
        return Stellaria.id("geo/crystalius.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Crystalius crystalius) {
        return Stellaria.id("textures/entity/crystalius.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Crystalius crystalius) {
        return Stellaria.id("animations/crystalius.animation.json");
    }
}
