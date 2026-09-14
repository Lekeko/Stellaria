package com.keko.stellaria.entity.boss.minions.einfarbig.client.Messier;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.minions.einfarbig.Messier;
import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
import com.keko.stellaria.entity.boss.minions.einfarbig.client.StarPhantom.StarPhantomModel;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MessierModel extends GeoModel<Messier> {
    @Override
    public ResourceLocation getModelResource(Messier asd) {
        return Stellaria.id("geo/star_phantom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Messier asd) {
        return Stellaria.id("textures/entity/star_phantom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Messier asd) {
        return Stellaria.id("animations/star_phantom.animation.json");
    }
}
