package com.keko.stellaria.entity.boss.zregos.client.renderers;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.boss.zregos.client.models.ZregosModel;
import com.keko.stellaria.entity.boss.zregos.Zregos;
import com.keko.stellaria.packet.ZregosLaserDamageC2S;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZregosRenderer extends GeoEntityRenderer<Zregos> {
    public ZregosRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Zregos zregos) {
        return Stellaria.id("textures/entity/zregos_head.png");
    }


    public static final float SIZE_MODIFIER = 3.0f;
    public static float laserTimer = 0.0f;
    private Vec3 laserDir = Vec3.ZERO;

    @Override
    protected void applyRotations(Zregos animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));

    }
    public static float amp = 1.0f;

    @Override
    public void preRender(PoseStack poseStack, Zregos animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(SIZE_MODIFIER, SIZE_MODIFIER, SIZE_MODIFIER);
        poseStack.translate(0, 0.5, 0);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(Zregos zregos, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int red = 255;
        int green = 255;
        int blue = 255;
        int alpha = 255;
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;


        poseStack.pushPose();

        if (amp < 0) amp = 0; //for some god forbid reason i need this here, dont ask why i dont even want to know why it goes to -67 skibidilions
        if (amp > 1) amp = 1;

        if (zregos.wantsToModifSky && amp > 0){
            amp -= Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()/23;
        }
        if (!zregos.wantsToModifSky && amp < 1){
            amp += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks()/23;
        }
        float easedAmp = Easing.EASE_IN_QUAD.ease(amp);

        ClientShaderHandler.renderZregosSkybox(easedAmp);
        ClientShaderHandler.renderClouds(easedAmp);



        super.render(zregos, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        poseStack.popPose();

        if (zregos.wantsToLaser){
            Vec3 cylEnd = zregos.getPosition(1.0f).add(0,2.7f,0);
            float yaw = Mth.lerp(partialTick, zregos.yRotO, zregos.getYRot());
            float pitch = Mth.lerp(partialTick, zregos.xRotO, zregos.getXRot());


            float yawRad = (float) Math.toRadians(yaw);
            float pitchRad = (float) Math.toRadians(pitch);

            Vec3 lookDir = new Vec3(
                    Mth.sin(yawRad) * Mth.cos(pitchRad),
                    Mth.sin(pitchRad),
                    Mth.cos(yawRad) * Mth.cos(pitchRad)
            );

            float laserThickness = Mth.clamp(laserTimer* 22/zregos.MAX_LASER_TIME, 0.0f, 1.0f);


            Vec3 cylBase = zregos.getPosition(partialTick)
                    .add(0, 5, 0)
                    .add(lookDir.scale(300));

            laserDir =  cylBase.subtract(cylEnd).normalize();
            zregos.laserDirection = laserDir;
            ClientPlayNetworking.send(new ZregosLaserDamageC2S(cylBase.toVector3f(), cylEnd.toVector3f(), zregos.getId()));
            ClientShaderHandler.renderZregosLaser(cylBase.x, cylBase.y, cylBase.z,cylEnd.x, cylEnd.y, cylEnd.z,laserThickness);

            laserTimer += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() ;


        }else {
            laserTimer = 0;
        }
        if (laserTimer == 0) {
            zregos.setLaserScreenAmp(zregos.getLaserScreenAmp() - Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 5f);
        }

        RenderSystem.enableDepthTest();
        ClientShaderHandler.renderLaserScreen(zregos.getLaserScreenAmp());
        RenderSystem.enableDepthTest();
    }

    @Override
    public void postRender(PoseStack poseStack, Zregos zregos, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);


    }




    @Override
    public @Nullable RenderType getRenderType(Zregos animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }



    @Override
    public boolean shouldRender(Zregos entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
