package com.keko.stellaria.entity.decour.zregosShatter.client.zregosShatter;

import com.keko.stellaria.Stellaria;
import com.keko.stellaria.entity.decour.zregosShatter.ZregosShatter;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class ZregosShatterRenderer extends EntityRenderer<ZregosShatter> {
    public ZregosShatterRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ZregosShatter entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        for (int cracks = 0; cracks < entity.getCrackList().size(); cracks++)
            renderQuad(entity, poseStack, entity.getCrackList().get(cracks), cracks);

        if (entity.getSize() < 100 && entity.tickCount < 80) {
            entity.addSize(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 2);
        }

        if (entity.tickCount > 80 && entity.getSize() >0) {
            entity.addSize(-Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 5);

        }
    }

    private void renderQuad(ZregosShatter entity, PoseStack poseStack, Vec3 direction, int cracks) {
        poseStack.pushPose();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        ResourceLocation crackTexture = Stellaria.id("textures/normal_vfx/crack.png");
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        float timeScale = entity.getSize()/100;
        float easedScale = (float) Math.pow(Easing.EASE_OUT_EXPO.ease(timeScale), .5f);



        double scale = 10 + cracks * 3;
        scale *= easedScale;
        Vec3 dir = direction.normalize();
        double length = 15 * scale;

        Vec3 pos1 = entity.getPosition(1.0f);
        Vec3 pos2 = pos1.add(dir.scale(length));
        double width = length * (200.0 / 1000.0);

        Vec3 side1 = dir.cross(new Vec3(1,0,0)).normalize().scale(width / 2);
        Vec3 side2 = side1.scale(-1);

        RenderSystem.setShaderTexture(0, crackTexture);
        Color color2 = new Color(127, 255, 245, 255);
        Color color1 = new Color(236, 110, 255, 255);

        Vec3 v1 = pos1.add(side1).subtract(cameraPos);
        Vec3 v2 = pos1.add(side2).subtract(cameraPos);
        Vec3 v3 = pos2.add(side2).subtract(cameraPos);
        Vec3 v4 = pos2.add(side1).subtract(cameraPos);
        buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(color2.getRed(), color2.getGreen(), color2.getBlue(), 255).setUv(0.0f, 1.0f);
        buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(color2.getRed(), color2.getGreen(), color2.getBlue(), 255).setUv(0.0f, 0.0f);
        buffer.addVertex(poseStack.last().pose(), (float) v3.x, (float) (v3.y + 1.2), (float) v3.z).setColor(color1.getRed(), color1.getGreen(), color1.getBlue(), 255).setUv(1.0f, 0.0f);
        buffer.addVertex(poseStack.last().pose(), (float) v4.x, (float) (v4.y + 1.2), (float) v4.z).setColor(color1.getRed(), color1.getGreen(), color1.getBlue(), 255).setUv(1.0f, 1.0f);

        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(ZregosShatter entity) {
        return null;
    }

    @Override
    public boolean shouldRender(ZregosShatter entity, Frustum frustum, double d, double e, double f) {
        return true;
    }
}
