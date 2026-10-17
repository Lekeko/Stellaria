package com.keko.stellaria.entity.boss.minions.einfarbig.client.StarPhantom;

import com.keko.stellaria.entity.boss.minions.einfarbig.StarPhantom;
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
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StarPhantomRenderer extends GeoEntityRenderer<StarPhantom> {
    public StarPhantomRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPhantomModel());
    }

    @Override
    protected void applyRotations(StarPhantom animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public void render(StarPhantom entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (!entity.getIsTeleporting()) {
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
    }

    @Override
    public void preRender(PoseStack poseStack, StarPhantom animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, LightTexture.FULL_BRIGHT, packedOverlay, colour);
        poseStack.translate(0, 5, 0);
        poseStack.scale(3, 3, 3);
    }

    @Override
    public void postRender(PoseStack poseStack, StarPhantom animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        RenderSystem.enableCull();

    }

    @Override
    public @Nullable RenderType getRenderType(StarPhantom animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(StarPhantom entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
