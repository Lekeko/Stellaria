package com.keko.stellaria.entity.projectiles.boss.zregos.client.renderers;

import com.keko.stellaria.entity.projectiles.boss.zregos.client.models.ZregosBoulderChainsModel;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosBouldersChains;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZregosBouldersChainsRenderer extends GeoEntityRenderer<ZregosBouldersChains> {
    public ZregosBouldersChainsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ZregosBoulderChainsModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosBouldersChains animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyRotations(ZregosBouldersChains animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRotationX()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getRotationY()));
    }

    @Override
    public void render(ZregosBouldersChains entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosBouldersChains animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }





    @Override
    public boolean shouldRender(ZregosBouldersChains entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
