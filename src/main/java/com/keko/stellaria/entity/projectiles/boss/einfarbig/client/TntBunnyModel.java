package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.TntBunny;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar.VoidStar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TntBunnyModel extends GeoModel<TntBunny> {
    @Override
    public ResourceLocation getModelResource(TntBunny tntBunny) {
        return Stellaria.id("geo/tnt_bunny.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TntBunny tntBunny) {
        return Stellaria.id("textures/entity/tnt_bunny.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TntBunny tntBunny) {
        return null;
    }
}
