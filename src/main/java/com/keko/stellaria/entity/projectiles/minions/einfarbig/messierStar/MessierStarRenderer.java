package com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.blocks.maceBlock.ZregosMaceBlockEntity;
import com.keko.stellaria.entity.projectiles.player.client.mage.amethystProjectile.AmethystProjectileModel;
import com.keko.stellaria.entity.projectiles.player.mage.AmethystProjectile;
import com.keko.stellaria.helper.UtilHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

import static foundry.veil.api.client.util.DebugRenderHelper.renderLine;

public class MessierStarRenderer extends GeoEntityRenderer<MessierStar> {

    public MessierStarRenderer(EntityRendererProvider.Context context) {
        super(context, new MessierStarModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MessierStar animatable) {
        return Stellaria.id("textures/entity/star_burst_" + animatable.getOrder() + ".png");
    }

    @Override
    public void render(MessierStar abstractArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        abstractArrow.setClientTicker(abstractArrow.getClientTicker() + g);

        poseStack.pushPose();
        poseStack.last().normal().identity();

        renderInBetweenStrip(abstractArrow,g, poseStack, multiBufferSource);
        poseStack.popPose();
        renderQuad(abstractArrow, poseStack, multiBufferSource);

    }

    private void renderQuad(MessierStar entity, PoseStack poseStack, MultiBufferSource bufferSource) {

        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();

        float rotationTime = 20;
        float rt = 1.0f - Mth.clamp(entity.getClientTicker()/ rotationTime, 0.0f, 1.0f);
        rt = Easing.EASE_IN_CIRC.ease(rt);
        poseStack.mulPose(Axis.XN.rotation(rt));

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        poseStack.translate(0, 0.5, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        PoseStack.Pose pose = poseStack.last();
        float sizeTime = 60;
        float t = Mth.clamp(entity.getClientTicker()/ sizeTime, 0.0f, 1.0f);
        t = Easing.EASE_OUT_ELASTIC.ease(t);

        if (entity.getWannaGo()){
            t = Mth.clamp((entity.getCliTickGo() - Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true))/ 10.0f, 0.0f, 1.0f);
            t = Easing.EASE_IN_CIRC.ease(t);

        }

        float size = 2.0f * t;

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityTranslucentEmissive(getTextureLocation(entity)));

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size, -size, 0).setUv(1, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size,  size, 0).setUv(1, 0).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix, -size,  size, 0).setUv(0, 0).setColor(255, 255, 255, 255);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();

    }

    private void renderInBetweenStrip(MessierStar entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        int segLen = 1;
        if (entity.getParentStar() == null) return;


        double laserLen = entity.position().distanceTo(entity.getParentStar().position());


        Vec3 dir = entity.getParentStar().position().subtract(entity.getPosition(partialTick)).normalize();
        Vec3 lastPos = entity.getPosition(partialTick);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(
                Stellaria.id("textures/normal_vfx/blank.png")
        ));

        float clampedFuckassValueForTheTrailBecauseILIKEMYSHITSMOOTH =
                Mth.clamp(entity.getClientTicker()/ 200.0f, 0.0f, 1.0f);

        float trailWidth = (2.0f - Easing.EASE_OUT_EXPO.ease(clampedFuckassValueForTheTrailBecauseILIKEMYSHITSMOOTH)) * 0.1f;

        if (entity.getWannaGo()){
            trailWidth *= entity.getCliTickGo()/10.0f;
        }

        for (double j = 0; j < laserLen; j += 1){
            float speed = 4.0f;
            float beamProgress = Math.min(entity.getClientTicker() * speed, (float) laserLen);

            float fadeLen = 1.0f;
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
            Color color2 = new Color(255, 255, 255, 255);

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
    public boolean shouldRender(MessierStar entity, Frustum frustum, double d, double e, double f) {
        return true;
    }



}
