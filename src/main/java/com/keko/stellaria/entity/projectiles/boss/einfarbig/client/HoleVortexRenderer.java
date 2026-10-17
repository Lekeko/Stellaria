package com.keko.stellaria.entity.projectiles.boss.einfarbig.client;

import com.keko.stellaria.entity.projectiles.boss.einfarbig.BunnyHat;
import com.keko.stellaria.entity.projectiles.boss.einfarbig.HoleVortex;
import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HoleVortexRenderer extends GeoEntityRenderer<HoleVortex> {
    public HoleVortexRenderer(EntityRendererProvider.Context context) {
        super(context, new HoleVortexModel());
    }

    @Override
    public void preRender(PoseStack poseStack, HoleVortex animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(HoleVortex entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        entity.setClientTicker(entity.getClientTicker() + partialTick);

        renderQuad(entity, poseStack, bufferSource);
    }

    private void renderQuad(HoleVortex entity, PoseStack poseStack, MultiBufferSource bufferSource) {

        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();

        float rotationTime = 20;
        float rt = 1.0f - Mth.clamp(entity.getClientTicker()/ rotationTime, 0.0f, 1.0f);
        rt = Easing.EASE_IN_CIRC.ease(rt);
        poseStack.mulPose(Axis.XN.rotation(rt));

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        poseStack.translate(0, 0.5, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        PoseStack.Pose pose = poseStack.last();
        float sizeTime = 60;
        float t = Mth.clamp(entity.getClientTicker()/ sizeTime, 0.0f, 1.0f);
        t = Easing.EASE_OUT_ELASTIC.ease(t);

        if (entity.getWannaGo()){
            t = Mth.clamp((entity.getCliTickGo() - Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true))/ 10.0f, 0.0f, 1.0f);
            t = Easing.EASE_IN_CIRC.ease(t);

        }

        float size = 10.0f * t;

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
}
