package com.keko.stellaria.entity.projectiles.boss.client.models.zregos;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosGhost;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosGhostModel extends GeoModel<ZregosGhost> {
    @Override
    public ResourceLocation getModelResource(ZregosGhost zregos) {
        return Stellaria.id("geo/zregos_head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosGhost zregos) {
        return Stellaria.id("textures/entity/zregos_head.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosGhost zregos) {
        return Stellaria.id("animations/zergos_ghost.animation.json");
    }
}
