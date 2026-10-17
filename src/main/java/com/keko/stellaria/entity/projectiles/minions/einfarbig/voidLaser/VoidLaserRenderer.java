package com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.packet.VoidLaserDamageC2S;
import com.keko.stellaria.packet.ZregosLaserDamageC2S;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VoidLaserRenderer extends ArrowRenderer<VoidLaser> {
    public VoidLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(VoidLaser abstractArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        renderLaser(abstractArrow, g, poseStack, multiBufferSource);
        abstractArrow.addClientTick(g);
    }

    private void renderLaser(VoidLaser abstractArrow, float g, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        if (abstractArrow.getOwner() == null) return;

        Vec3 laserbase = abstractArrow.getPosition(g);
        Vec3 dir = abstractArrow.getClientDirection(g);
        float dist = 300.0f;
        Vec3 laserEnd = laserbase.add(dir.scale(dist));
        float sizeTime = 20.0f;
        float t = Mth.clamp(abstractArrow.getClientTick() / sizeTime, 0.0f, 1.0f);
        t = Easing.EASE_IN_BOUNCE.ease(t);

        if (abstractArrow.getWannaGo())
            abstractArrow.setGoSizeTick(abstractArrow.getGoSizeTick() - g);

        float tt = Mth.clamp(abstractArrow.getGoSizeTick() / 10.0f, 0.0f, 1.0f);
        tt = Easing.EASE_OUT_CUBIC.ease(tt);


        float laserThickness = 1.0f * t * tt;

        ClientShaderHandler.voidLaserShader(laserbase.x, laserbase.y, laserbase.z, laserEnd.x, laserEnd.y, laserEnd.z, laserThickness);
        ClientPlayNetworking.send(new VoidLaserDamageC2S(laserbase.toVector3f(), laserEnd.toVector3f(), abstractArrow.getOwner().getId()));

    }


    @Override
    public boolean shouldRender(VoidLaser entity, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(VoidLaser entity) {
        return Stellaria.id("textures/block/void_lamp.png"); //just to keep sure it dont crash mmwmwmwm
    }
}
