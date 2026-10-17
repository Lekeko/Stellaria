package com.keko.stellaria.entity.projectiles.minions.einfarbig.VoidLaserWarn;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.voidLaser.VoidLaser;
import com.keko.stellaria.packet.VoidLaserDamageC2S;
import com.keko.stellaria.shader.ClientShaderHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class VoidLaserWarnRenderer extends ArrowRenderer<VoidLaserWarn> {
    public VoidLaserWarnRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(VoidLaserWarn abstractArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        renderLaser(abstractArrow, g, poseStack, multiBufferSource);
        abstractArrow.setClientTicker(abstractArrow.getClientTicker() + g);

        renderQuad(abstractArrow, poseStack, multiBufferSource);
    }

    private void renderLaser(VoidLaserWarn abstractArrow, float g, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        if (abstractArrow.getOwner() == null) return;
        Vec3 laserEnd = abstractArrow.position().add(0.0f, 300.0f, 0.0f);
        Vec3 laserbase = abstractArrow.position();

        ClientShaderHandler.voidLaserShader(laserbase.x, laserbase.y, laserbase.z, laserEnd.x, laserEnd.y, laserEnd.z, 0.1f);

    }

    private void renderQuad(VoidLaserWarn entity, PoseStack poseStack, MultiBufferSource bufferSource) {

        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();
        poseStack.translate(0.0f, 2.1f, 0.0f);
        float rotationTime = 60;
        float rt = 1.0f - Mth.clamp(entity.getClientTicker()/ rotationTime, 0.0f, 1.0f);
        rt = Easing.EASE_OUT_QUAD.ease(rt);
        poseStack.mulPose(Axis.YN.rotation(rt));

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        poseStack.translate(0, 0.5, 0);

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        PoseStack.Pose pose = poseStack.last();
        float sizeTime = 60;
        float t = Mth.clamp(entity.getClientTicker()/ sizeTime, 0.0f, 1.0f);
        t = Easing.EASE_OUT_QUAD.ease(t);

        float size = 10.0f * Mth.sin(t * Mth.PI);

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityTranslucentEmissive(getTextureLocation(entity)));

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size, -size, 0).setUv(1, 1).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix,  size,  size, 0).setUv(1, 0).setColor(255, 255, 255, 255);
        buffer.addVertex(matrix, -size,  size, 0).setUv(0, 0).setColor(255, 255, 255, 255);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, getTextureLocation(entity));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        poseStack.popPose();

    }


    @Override
    public boolean shouldRender(VoidLaserWarn entity, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(VoidLaserWarn entity) {
        return Stellaria.id("textures/normal_vfx/laser_summon.png");
    }
}
