package com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj;

import com.keko.stellaria.helper.RenderUtilHelper;
import com.keko.stellaria.helper.Trail;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.awt.*;

public class PlayingCardProjectileRenderer extends GeoEntityRenderer<PlayingCardProjectile> {
    Trail trail = new Trail(200, this.animatable);

    public PlayingCardProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayingCardProjectileModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PlayingCardProjectile animatable) {
        return super.getTextureLocation(animatable);
    }

    @Override
    protected void applyRotations(PlayingCardProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public void preRender(PoseStack poseStack, PlayingCardProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float sizeModif = 1.5f;

        poseStack.scale(sizeModif, sizeModif, sizeModif);
    }

    @Override
    public void render(PlayingCardProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);


        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, Color.WHITE, Color.BLACK, trail);

    }

    @Override
    public @Nullable RenderType getRenderType(PlayingCardProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(PlayingCardProjectile entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
