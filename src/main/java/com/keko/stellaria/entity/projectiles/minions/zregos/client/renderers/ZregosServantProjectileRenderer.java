package com.keko.stellaria.entity.projectiles.minions.zregos.client.renderers;

import com.keko.stellaria.entity.projectiles.minions.zregos.client.models.ZregosServantProjectileModel;
import com.keko.stellaria.entity.projectiles.minions.zregos.ZregosServantProjectile;
import com.keko.stellaria.helper.RenderUtilHelper;
import com.keko.stellaria.helper.Trail;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.keko.stellaria.helper.MathHelpers.lerp;

public class ZregosServantProjectileRenderer extends GeoEntityRenderer<ZregosServantProjectile> {

    public ZregosServantProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosServantProjectileModel());
    }

    @Override
    public void render(ZregosServantProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        Color color1 = new Color(87, 255, 221, 255);
        Color color2 = new Color(195, 79, 255, 238);

        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, color1, color2, entity.getTrail(), Vec3.ZERO, 1.0f, Easing.LINEAR);
    }

    @Override
    public void postRender(PoseStack poseStack, ZregosServantProjectile animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosServantProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(3,3,3);
        poseStack.translate(0,0.8,0);
    }

    @Override
    protected void applyRotations(ZregosServantProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) + 180));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 180f));
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosServantProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }

}
