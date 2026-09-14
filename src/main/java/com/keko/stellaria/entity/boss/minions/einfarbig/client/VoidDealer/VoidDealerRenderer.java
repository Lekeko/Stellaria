package com.keko.stellaria.entity.boss.minions.einfarbig.client.VoidDealer;

import com.keko.stellaria.entity.boss.minions.einfarbig.VoidDealer;
import com.mojang.blaze3d.systems.RenderSystem;
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

public class VoidDealerRenderer extends GeoEntityRenderer<VoidDealer> {
    public VoidDealerRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidDealerModel());
    }

    @Override
    protected void applyRotations(VoidDealer animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public void render(VoidDealer entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        RenderSystem.disableCull();
        float damageTicker = entity.getDamageTicker();
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
    public void preRender(PoseStack poseStack, VoidDealer animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, LightTexture.FULL_BRIGHT, packedOverlay, colour);
        poseStack.scale(5, 5, 5);
    }

    @Override
    public void postRender(PoseStack poseStack, VoidDealer animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        RenderSystem.enableCull();

    }

    @Override
    public @Nullable RenderType getRenderType(VoidDealer animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(VoidDealer entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
