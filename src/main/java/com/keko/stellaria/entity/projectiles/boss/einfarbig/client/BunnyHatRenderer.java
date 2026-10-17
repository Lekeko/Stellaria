package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BunnyHatRenderer extends GeoEntityRenderer<BunnyHat> {
    public BunnyHatRenderer(EntityRendererProvider.Context context) {
        super(context, new BunnyHatModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BunnyHat animatable) {
        if (animatable.getPhase() == 2)
            return Stellaria.id("textures/entity/bunny_hat_mono.png");
        return super.getTextureLocation(animatable);
    }

    @Override
    public void preRender(PoseStack poseStack, BunnyHat animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        float scaleFactor = 5.0f;
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
    }

    @Override
    public void render(BunnyHat entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }
}
