package com.keko.stellaria.blocks.portal;

import com.keko.stellaria.Stellaria;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DraconicCrystalBlockEntityModel extends GeoModel<DraconicCrystalBlockEntity> {
    @Override
    public ResourceLocation getModelResource(DraconicCrystalBlockEntity animatable) {
        return Stellaria.id("geo/draconic_crystal_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DraconicCrystalBlockEntity animatable) {
        return Stellaria.id("textures/block/draconic_crystal_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DraconicCrystalBlockEntity animatable) {
        return Stellaria.id("animations/draconic_crystal_block.animation.json");    }
}
