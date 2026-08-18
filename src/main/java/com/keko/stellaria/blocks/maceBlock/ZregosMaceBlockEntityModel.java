package com.keko.stellaria.blocks.maceBlock;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ZregosMaceBlockEntityModel extends GeoModel<ZregosMaceBlockEntity> {
    @Override
    public ResourceLocation getModelResource(ZregosMaceBlockEntity animatable) {
        return Stellaria.id("geo/zregos_mace_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZregosMaceBlockEntity animatable) {
        return Stellaria.id("textures/block/zregos_mace_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZregosMaceBlockEntity animatable) {
        return Stellaria.id("animations/zregos_mace.animation.json");
    }
}
