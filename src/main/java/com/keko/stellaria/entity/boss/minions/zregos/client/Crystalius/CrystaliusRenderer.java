package com.keko.stellaria.entity.boss.minions.zregos.client.Crystalius;

import com.keko.stellaria.entity.boss.minions.zregos.Crystalius;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public class CrystaliusRenderer extends GeoEntityRenderer<Crystalius> {
    public CrystaliusRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystaliusModel());
    }

    private static final Color COLOR1 = new Color(0, 255, 255);
    private static final Color COLOR2 = new Color(255, 0, 255);

    @Override
    public void preRender(PoseStack poseStack, Crystalius animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.translate(0,0.5,0);
        poseStack.scale(2,2,2);
    }

    @Override
    public void render(Crystalius entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        renderSaturnLikeCircle(entity, entityYaw, partialTick, poseStack, bufferSource);//i love saturn

        float damageTicker = entity.getDamagedTicker();
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

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static Color lerpColor(Color c1, Color c2, float t) {
        int r = Math.round(lerp(c1.getRed(), c2.getRed(), t));
        int g = Math.round(lerp(c1.getGreen(), c2.getGreen(), t));
        int b = Math.round(lerp(c1.getBlue(), c2.getBlue(), t));
        return new Color(r, g, b);
    }

    private static Color getRingColor(float progress) {
        float t = 1.0f - Math.abs(2.0f * progress - 1.0f);
        return lerpColor(COLOR1, COLOR2, t);
    }

    private void renderSaturnLikeCircle(Crystalius entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
        float segmentSpacer = 2.0f;
        float CIRCLERAD = 360;
        float spacedRadiu = 3.0f;
        float diskRadiu = spacedRadiu + 1.2f;
        float alpha = 1.0f;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTick) * 4.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(15));


        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = poseStack.last().pose();
        for (int i = 0; i < CIRCLERAD; i += segmentSpacer) {
            float angle1 = (float) Math.toRadians(i);
            float angle2 = (float) Math.toRadians(i + segmentSpacer);

            float innerX1 = Mth.cos(angle1) * spacedRadiu;
            float innerZ1 = Mth.sin(angle1) * spacedRadiu;
            float outerX1 = Mth.cos(angle1) * diskRadiu;
            float outerZ1 = Mth.sin(angle1) * diskRadiu;

            float innerX2 = Mth.cos(angle2) * spacedRadiu;
            float innerZ2 = Mth.sin(angle2) * spacedRadiu;
            float outerX2 = Mth.cos(angle2) * diskRadiu;
            float outerZ2 = Mth.sin(angle2) * diskRadiu;

            float progress1 = i / CIRCLERAD;
            float progress2 = (i + segmentSpacer) / CIRCLERAD;

            Color color1 = getRingColor(progress1);
            Color color2 = getRingColor(progress2);

            float r1 = color1.getRed() / 255f;
            float g1 = color1.getGreen() / 255f;
            float b1 = color1.getBlue() / 255f;

            float r2 = color2.getRed() / 255f;
            float g2 = color2.getGreen() / 255f;
            float b2 = color2.getBlue() / 255f;

            float y = 1.5f;

            buffer.addVertex(matrix, innerX1, y, innerZ1).setColor(r1, g1, b1, alpha);
            buffer.addVertex(matrix, outerX1, y, outerZ1).setColor(r1, g1, b1, alpha);
            buffer.addVertex(matrix, outerX2, y, outerZ2).setColor(r2, g2, b2, alpha);
            buffer.addVertex(matrix, innerX2, y, innerZ2).setColor(r2, g2, b2, alpha);

            buffer.addVertex(matrix, innerX2, y, innerZ2).setColor(r2, g2, b2, alpha);
            buffer.addVertex(matrix, outerX2, y, outerZ2).setColor(r2, g2, b2, alpha);
            buffer.addVertex(matrix, outerX1, y, outerZ1).setColor(r1, g1, b1, alpha);
            buffer.addVertex(matrix, innerX1, y, innerZ1).setColor(r1, g1, b1, alpha);

        }

        poseStack.popPose();
    }

    @Override
    public void postRender(PoseStack poseStack, Crystalius animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public @Nullable RenderType getRenderType(Crystalius animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.text(getTextureLocation(animatable));
    }
}
