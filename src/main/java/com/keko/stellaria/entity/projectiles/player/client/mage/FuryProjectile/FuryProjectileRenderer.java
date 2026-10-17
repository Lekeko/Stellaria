package com.keko.stellaria.entity.projectiles.player.client.mage.FuryProjectile;

import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import com.keko.stellaria.helper.RenderUtilHelper;
import com.keko.stellaria.helper.Trail;
import com.keko.stellaria.world.dimension.ModDimensions;
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
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.keko.stellaria.helper.MathHelpers.lerp;

public class FuryProjectileRenderer extends GeoEntityRenderer<FuryProjectile> {


    public FuryProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new FuryProjectileModel());
    }

    @Override
    protected void applyRotations(FuryProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot())));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 180f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getClientTick()));

    }

    @Override
    public void preRender(PoseStack poseStack, FuryProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        if (animatable.level().dimension().equals(ModDimensions.ARTHA_LEVEL_KEY) || animatable.level().dimension().equals(ModDimensions.EBONY_LEVEL_KEY))
            poseStack.scale(2.0f,2.0f,2.0f);
    }

    @Override
    public void render(FuryProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        entity.addClientTick(partialTick);

        java.awt.Color color1 = new java.awt.Color(87, 255, 221, 255);
        java.awt.Color color2 = new java.awt.Color(255, 0, 255, 238);


        poseStack.pushPose();
        poseStack.last().pose().identity();

        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, color1, color2, entity.getTrail(), Vec3.ZERO, 1.0f, Easing.LINEAR);

        poseStack.popPose();


    }

    @Override
    public boolean shouldRender(FuryProjectile entity, Frustum frustum, double d, double e, double f) {
        return entity.tickCount > 1;
    }

    @Override
    public @Nullable RenderType getRenderType(FuryProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
