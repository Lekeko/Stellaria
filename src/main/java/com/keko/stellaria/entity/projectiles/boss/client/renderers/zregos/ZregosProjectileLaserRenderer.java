package com.keko.stellaria.entity.projectiles.boss.client.renderers.zregos;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosProjectileLaser;
import com.keko.stellaria.helper.UtilHelper;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class ZregosProjectileLaserRenderer extends EntityRenderer<ZregosProjectileLaser> {
    public ZregosProjectileLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ZregosProjectileLaser entity) {
        return Stellaria.id("textures/entity/zregos_projectile_laser.png");
    }

    @Override
    public void render(ZregosProjectileLaser entity, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, partialTick, poseStack, multiBufferSource, i);

        entity.setClientTicker(entity.getClientTicker() + partialTick);

        int segLen = 1;
        double laserLen = entity.position().distanceTo(entity.getShootPos());


        Vec3 dir = entity.getShootPos().subtract(entity.getPosition(partialTick)).normalize();
        Vec3 lastPos = entity.getPosition(partialTick);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(
                RenderType.entityTranslucent(getTextureLocation(entity))
        );

        float clampedFuckassValueForTheTrailBecauseILIKEMYSHITSMOOTH =
                Mth.clamp(entity.getClientTicker()/ 200.0f, 0.0f, 1.0f);

        float trailWidth = (1.0f - Easing.EASE_OUT_EXPO.ease(clampedFuckassValueForTheTrailBecauseILIKEMYSHITSMOOTH)) * 15;

        for (double j = 0; j < laserLen; j += 1){
            float speed = 9.0f;
            float beamProgress = Math.min(entity.getClientTicker() * speed, (float) laserLen);

            float fadeLen = 6.0f;
            float dist1 = (float) (beamProgress - j);
            float dist2 = (float) (beamProgress - (j + 1));

            float normalizedProgr1 = Mth.clamp(dist1/ fadeLen, 0.0f, 1.0f);
            float normalizedProgr2 = Mth.clamp(dist2/ fadeLen, 0.0f, 1.0f);

            Vec3 newPos = lastPos.add(dir.scale(segLen));

            Vec3 cameraLookVec = new Vec3(camera.getLookVector());
            Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(trailWidth / 2);
            Vec3 side2 = side1.scale(-1);


            Vec3 v1 = lastPos.add(side1);
            Vec3 v2 = lastPos.add(side2);
            Vec3 v3 = newPos.add(side2);
            Vec3 v4 = newPos.add(side1);

            Vec3 entityPos = entity.getPosition(partialTick);

            v1 = v1.subtract(entityPos);
            v2 = v2.subtract(entityPos);
            v3 = v3.subtract(entityPos);
            v4 = v4.subtract(entityPos);

            Color color1 = new Color(255, 255, 255, 255);
            Color color2 = new Color(242, 62, 255, 255);

            Color finalCol = UtilHelper.lerpColor(color1, color2, (float) Math.pow(clampedFuckassValueForTheTrailBecauseILIKEMYSHITSMOOTH, 2f));

            int red = finalCol.getRed();
            int green = finalCol.getGreen();
            int blue = finalCol.getBlue();
            int alpha1 = (int) (finalCol.getAlpha() * normalizedProgr1);
            int alpha2 = (int) (finalCol.getAlpha() * normalizedProgr2);

            PoseStack.Pose pose = poseStack.last();
            vertexConsumer.addVertex(pose.pose(), (float)v1.x, (float)v1.y, (float)v1.z)
                    .setColor(red, green, blue, alpha1)
                    .setUv(0f, 0f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(pose, 0, 1, 0);

            vertexConsumer.addVertex(pose.pose(), (float)v2.x, (float)v2.y, (float)v2.z)
                    .setColor(red, green, blue, alpha1)
                    .setUv(1f, 0f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(pose, 0, 1, 0);

            vertexConsumer.addVertex(pose.pose(), (float)v3.x, (float)v3.y, (float)v3.z)
                    .setColor(red, green, blue, alpha2)
                    .setUv(1f, 1f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(pose, 0, 1, 0);

            vertexConsumer.addVertex(pose.pose(), (float)v4.x, (float)v4.y, (float)v4.z)
                    .setColor(red, green, blue, alpha2)
                    .setUv(0f, 1f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setNormal(pose, 0, 1, 0);

            lastPos = newPos;
        }

    }

    @Override
    public boolean shouldRender(ZregosProjectileLaser entity, Frustum frustum, double d, double e, double f) {
        return true;
    }


}
