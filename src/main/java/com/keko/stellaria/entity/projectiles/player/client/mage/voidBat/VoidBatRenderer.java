package com.keko.stellaria.entity.projectiles.player.client.mage.voidBat;

import com.keko.stellaria.entity.projectiles.player.mage.FuryProjectile;
import com.keko.stellaria.entity.projectiles.player.mage.VoidBat;
import com.keko.stellaria.helper.RenderUtilHelper;
import com.keko.stellaria.world.dimension.ModDimensions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
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

import java.awt.*;

public class VoidBatRenderer extends GeoEntityRenderer<VoidBat> {


    public VoidBatRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidBatModel());
    }

    @Override
    protected void applyRotations(VoidBat animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot())));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 180f));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180f));


    }

    @Override
    public void preRender(PoseStack poseStack, VoidBat animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        if (animatable.level().dimension().equals(ModDimensions.ARTHA_LEVEL_KEY) || animatable.level().dimension().equals(ModDimensions.EBONY_LEVEL_KEY))
            poseStack.scale(2.0f,2.0f,2.0f);
    }

    @Override
    public void render(VoidBat entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        poseStack.pushPose();
        poseStack.last().pose().identity();

        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, Color.WHITE, Color.BLACK, entity.getTrail(), Vec3.ZERO, 0.2f, Easing.LINEAR);

        poseStack.popPose();


    }

    @Override
    public boolean shouldRender(VoidBat entity, Frustum frustum, double d, double e, double f) {
        return entity.tickCount > 1;
    }

    @Override
    public @Nullable RenderType getRenderType(VoidBat animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
