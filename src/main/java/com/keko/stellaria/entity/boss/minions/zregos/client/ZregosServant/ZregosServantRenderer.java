package com.keko.stellaria.entity.boss.minions.zregos.client.ZregosServant;

import com.keko.stellaria.entity.boss.minions.zregos.ZregosServant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZregosServantRenderer extends GeoEntityRenderer<ZregosServant> {
    public ZregosServantRenderer(EntityRendererProvider.Context context) {
        super(context, new ZregosServantModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ZregosServant animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.translate(0,0.5,0);
        poseStack.scale(2,2,2);
    }

    @Override
    public void render(ZregosServant entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        float damageTicker = entity.getDamagedTicker();
        if (damageTicker > 0) {
            int alpha = 100;

            this.reRender(
                    this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),
                    poseStack,
                    bufferSource,
                    entity,
                    RenderType.text(getTextureLocation(entity)),
                    bufferSource.getBuffer(RenderType.text(getTextureLocation(entity))),
                    partialTick,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    (alpha << 24) | 0x00FF0000
            );
        }
    }

    @Override
    public void postRender(PoseStack poseStack, ZregosServant animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderType getRenderType(ZregosServant animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }

    @Override
    public boolean shouldRender(ZregosServant entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
