package com.keko.stellaria.entity.projectiles.player.client.ranged;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.entity.projectiles.player.ranged.CosmicArrow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CosmicArrowModel extends GeoModel<CosmicArrow> {
    @Override
    public ResourceLocation getModelResource(CosmicArrow animatable) {
        return Stellaria.id("geo/cosmic_arrow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CosmicArrow animatable) {
        return Stellaria.id("textures/entity/cosmic_arrow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CosmicArrow animatable) {
        return null;
    }
}
