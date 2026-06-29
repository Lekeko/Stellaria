package com.keko.stellaria.entity.projectiles.player.client.explosive.EndCrystalProjectile;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.explosive.CosmicShieldProjectile;
import com.keko.stellaria.entity.projectiles.player.explosive.EndCrystalProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndCrystalProjectileRenderer extends GeoEntityRenderer<EndCrystalProjectile> {

    public EndCrystalProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new EndCrystalProjectileModel());
    }

    @Override
    public void render(EndCrystalProjectile entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    @Override
    public void preRender(PoseStack poseStack, EndCrystalProjectile animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        poseStack.translate(0, 1, 0);
    }

    @Override
    protected void applyRotations(EndCrystalProjectile animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }

    @Override
    public @Nullable RenderType getRenderType(EndCrystalProjectile animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
