package com.keko.stellaria.entity.projectiles.player.client.melee;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.player.melee.MeleeSlash;
import com.keko.stellaria.logic.StellariaConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class MeleeSlashRenderer extends GeoEntityRenderer<MeleeSlash> {
    public MeleeSlashRenderer(EntityRendererProvider.Context context) {
        super(context, new MeleeSlashModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MeleeSlash animatable) {
        if (animatable.getSlashType() == StellariaConstants.SERPENTS_BLADE.ordinal())
            return Stellaria.id("textures/entity/slash_attack_melee_zregos.png");
        if (animatable.getSlashType() == StellariaConstants.VOID_GLAIVE.ordinal())
            return Stellaria.id("textures/entity/slash_attack_melee_einfarbig.png");

        return super.getTextureLocation(animatable);
    }

    @Override
    protected void applyRotations(MeleeSlash animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot())));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));
        poseStack.mulPose(Axis.ZN.rotationDegrees(animatable.getRotation()));

    }

    @Override
    public Color getRenderColor(MeleeSlash animatable, float partialTick, int packedLight) {
        int red = 255;
        int green = 255;
        int blue = 255;
        float normalAlpha = Mth.clamp(1 - (animatable.tickCount/20.0f * 1.5f), 0.0f, 1.0f);
        float easedAlpha = Easing.EASE_OUT_QUAD.ease(normalAlpha);

        int alpha = (int) (255 * easedAlpha);
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        return new Color(color);
    }

    @Override
    public void render(MeleeSlash entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        RenderSystem.disableCull();
        entity.setClientTickerINeed(entity.getClientTickerINeed() + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());



        if (entity.tickCount > 1) {
            renderSlashOnType(entity.getSlashType(), entity, entityYaw, partialTick, poseStack, bufferSource);
        }

        poseStack.popPose();

    }

    private void renderSlashOnType(int slashType, MeleeSlash entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        float wideness =10.4f;
        float anim = Mth.sin(entity.getClientTickerINeed()) * wideness;

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);
    }

    @Override
    public void preRender(PoseStack poseStack, MeleeSlash animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(2.0f,2.0f,2.0f);
        if (animatable.getSlashType() == StellariaConstants.VOID_GLAIVE.ordinal())
            poseStack.scale(2.0f,2.0f,2.0f);
    }

    @Override
    public void postRender(PoseStack poseStack, MeleeSlash animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderType getRenderType(MeleeSlash animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(texture);
    }
}
