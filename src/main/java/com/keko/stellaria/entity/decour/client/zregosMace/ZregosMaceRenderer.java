package com.keko.stellaria.entity.decour.client.zregosMace;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.decour.zregosMace.ZregosMace;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public class ZregosMaceRenderer extends GeoEntityRenderer<ZregosMace> {
    public ZregosMaceRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosMaceModel());
    }

    @Override
    public void render(ZregosMace entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        poseStack.pushPose();
        renderInBetweenChain(entity, partialTick, poseStack, bufferSource);
        poseStack.popPose();

        entity.addClientTick(partialTick);

    }

    private void renderInBetweenChain(ZregosMace entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        for (ZregosMace zregosMace : entity.getConnections()){
            Vec3 startPos = entity.getPosition(partialTick).add(0,entity.getBbHeight()/2, 0);
            Vec3 endPos = zregosMace.getPosition(partialTick).add(0,entity.getBbHeight()/2, 0);

            Vec3 dir = endPos.subtract(startPos);

            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

            float chainLen = 1;
            for (int i = 0; i < dir.length(); i += chainLen){
                Vec3 startPosSeg = startPos.lerp(endPos, i/ dir.length());
                Vec3 endPosSeg = startPosSeg.add(dir.normalize());

                float chainWidth = 1f;
                Vec3 cameraLookVec = new Vec3(camera.getLookVector());
                Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(chainWidth / 2);
                Vec3 side2 = side1.scale(-1);

                Vec3 v1 = startPosSeg.add(side1);
                Vec3 v2 = startPosSeg.add(side2);
                Vec3 v3 = endPosSeg.add(side2);
                Vec3 v4 = endPosSeg.add(side1);

                Vec3 entityPos = entity.getPosition(partialTick);
                v1 = v1.subtract(entityPos);
                v2 = v2.subtract(entityPos);
                v3 = v3.subtract(entityPos);
                v4 = v4.subtract(entityPos);

                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(
                        Stellaria.id("textures/normal_vfx/zregos_mace_chain.png")
                ));

                Color chainColor = new Color(255, 255, 255, 255);
                int red = chainColor.getRed();
                int green = chainColor.getGreen();
                int blue = chainColor.getBlue();
                int alpha = chainColor.getAlpha();

                PoseStack.Pose pose = poseStack.last();
                vertexConsumer.addVertex(pose.pose(), (float) v1.x, (float) v1.y, (float) v1.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(0f, 1f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v2.x, (float) v2.y, (float) v2.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(0f, 0f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v3.x, (float) v3.y, (float) v3.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(1f, 0f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);
                vertexConsumer.addVertex(pose.pose(), (float) v4.x, (float) v4.y, (float) v4.z)
                        .setColor(red, green, blue, alpha)
                        .setUv(1f, 1f)
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(LightTexture.FULL_BRIGHT)
                        .setNormal(pose, 0, 1, 0);

            }
        }
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosMace animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.translate(0,animatable.getBbHeight()/2, 0);
        poseStack.scale(2,2,2);

    }

    @Override
    public void postRender(PoseStack poseStack, ZregosMace animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    protected void applyRotations(ZregosMace animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        float slow = 10;
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getClientTick()/slow));
        poseStack.mulPose(Axis.ZN.rotationDegrees(animatable.getClientTick()/slow));
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosMace animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }
}
