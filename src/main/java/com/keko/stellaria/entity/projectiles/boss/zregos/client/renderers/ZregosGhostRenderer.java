package com.keko.stellaria.entity.projectiles.boss.zregos.client.renderers;

import com.keko.stellaria.entity.projectiles.boss.zregos.client.models.ZregosGhostModel;
import com.keko.stellaria.entity.projectiles.boss.zregos.ZregosGhost;
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

public class ZregosGhostRenderer extends GeoEntityRenderer<ZregosGhost> {
    public ZregosGhostRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ZregosGhostModel());
    }
    private final float ANIM_LEN = 2 * 20;

    public float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }

    @Override
    protected void applyRotations(ZregosGhost animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(animatable.getRotation()));

    }

    @Override
    public void preRender(PoseStack poseStack, ZregosGhost animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.scale(4.0f,4.0f,4.0f);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

    }

    @Override
    public void render(ZregosGhost zregosGhost, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i){

        poseStack.pushPose();

        super.render(zregosGhost, entityYaw, partialTick, poseStack, multiBufferSource, LightTexture.FULL_BRIGHT);

        poseStack.popPose();
    }

    @Override
    public void postRender(PoseStack poseStack, ZregosGhost animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosGhost animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(texture);
    }

    @Override
    public boolean shouldRender(ZregosGhost zregosGhost, Frustum frustum, double d, double e, double f) {
        return true;
    }


}
