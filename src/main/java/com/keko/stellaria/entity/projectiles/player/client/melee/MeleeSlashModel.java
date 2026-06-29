package com.keko.stellaria.entity.projectiles.player.client.melee;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MeleeSlashModel extends GeoModel<MeleeSlash> {
    @Override
    public ResourceLocation getModelResource(MeleeSlash animatable) {
        return Stellaria.id("geo/slash_attack_melee.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MeleeSlash animatable) {
        return Stellaria.id("textures/entity/slash_attack_melee_vanila.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MeleeSlash animatable) {
        return null;
    }
}
