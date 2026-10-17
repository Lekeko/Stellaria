package com.keko.stellaria.entity.projectiles.minions.einfarbig.voidStar;

import com.keko.stellaria.entity.projectiles.minions.einfarbig.messierStar.MessierStar;
import com.keko.stellaria.helper.RenderUtilHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public class VoidStarRenderer extends GeoEntityRenderer<VoidStar> {
    public VoidStarRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidStarModel());
    }

    @Override
    public void render(VoidStar entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderUtilHelper.renderTrail(entity, partialTick, poseStack, Color.WHITE, Color.BLACK, entity.getTrail(), Vec3.ZERO,1.0f, Easing.LINEAR);
        renderQuad(entity, poseStack, bufferSource);
    }

    private void renderQuad(VoidStar entity, PoseStack poseStack, MultiBufferSource bufferSource) {

        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();


        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(0, 0.5, 0);

        PoseStack.Pose pose = poseStack.last();
        float size = 2.0f;

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
    public boolean shouldRender(VoidStar entity, Frustum frustum, double d, double e, double f) {
        return true;
    }

}
