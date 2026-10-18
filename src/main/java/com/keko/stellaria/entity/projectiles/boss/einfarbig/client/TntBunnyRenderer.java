package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.entity.projectiles.boss.einfarbig.TntBunny;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectile;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.playingCardProj.PlayingCardProjectileModel;
import com.keko.stellaria.helper.RenderUtilHelper;
import com.keko.stellaria.helper.Trail;
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

public class TntBunnyRenderer extends GeoEntityRenderer<TntBunny> {
    Trail trail = new Trail(200, this.animatable);

    public TntBunnyRenderer(EntityRendererProvider.Context context) {
        super(context, new TntBunnyModel());
    }

    @Override
    public ResourceLocation getTextureLocation(TntBunny animatable) {
        return super.getTextureLocation(animatable);
    }

    @Override
    protected void applyRotations(TntBunny animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        Vec3 dir = animatable.getDeltaMovement().normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-dir.x, -dir.z));
        float pitch = (float) Math.toDegrees(Math.asin(Mth.clamp(dir.y, -1.0, 1.0)));

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
    }

    @Override
    public void preRender(PoseStack poseStack, TntBunny animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float sizeModif = 2.5f;

        poseStack.scale(sizeModif, sizeModif, sizeModif);
    }

    @Override
    public void render(TntBunny entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, Color.WHITE, Color.RED, entity.getTrail(), Vec3.ZERO,1.0f, Easing.LINEAR);
    }

    @Override
    public @Nullable RenderType getRenderType(TntBunny animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }

    @Override
    public boolean shouldRender(TntBunny entity, Frustum frustum, double d, double e, double f) {
        return entity.tickCount > 1;
    }
}
