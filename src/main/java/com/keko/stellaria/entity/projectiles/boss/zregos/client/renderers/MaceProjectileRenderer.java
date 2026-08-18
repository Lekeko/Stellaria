package com.keko.stellaria.entity.projectiles.boss.zregos.client.renderers;

import com.keko.stellaria.entity.projectiles.boss.zregos.client.models.MaceProjectileModel;
import com.keko.stellaria.entity.projectiles.boss.zregos.MaceProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MaceProjectileRenderer extends GeoEntityRenderer<MaceProjectile> {
    public MaceProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new MaceProjectileModel());
    }

    @Override
    public void render(MaceProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    @Override
    public void preRender(PoseStack poseStack, MaceProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void postRender(PoseStack poseStack, MaceProjectile animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyRotations(MaceProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) + 180));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 180f));

    }

    @Override
    public @Nullable RenderType getRenderType(MaceProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(MaceProjectile entity, Frustum frustum, double d, double e, double f) {
        return entity.tickCount > 1;
    }
}
