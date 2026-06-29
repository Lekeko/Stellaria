package com.keko.stellaria.renderHelpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.Random;

public class ScreenHelpers {
    public static Vec3 worldToScreen(Vec3 pos, GuiGraphics guiGraphics){
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        //matrix for translation
        Matrix4f projMat = VeilRenderSystem.renderer().getCameraMatrices().getProjectionMatrix();
        Matrix4f viewMat = VeilRenderSystem.renderer().getCameraMatrices().getViewMatrix();

        Vec3 relativePositionFromCamera = pos.subtract(camera.getPosition()); //self-explanatory //fk u mean "This word is normally spelled with a hyphen."
        Vector4f screenPosition = new Vector4f((float) relativePositionFromCamera.x, (float) relativePositionFromCamera.y, (float) relativePositionFromCamera.z, 1);

        screenPosition = screenPosition.mul(viewMat); //A guy said to do this on reddit
        screenPosition = screenPosition.mul(projMat); //get the fov from proj so it scales with fov from setting

        if (screenPosition.w <= 0.001f) {
            return null;
        }

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        //divide for perspective and scale from clip space, i think
        float screenX = (screenPosition.x / screenPosition.z) * width/2;
        float screenY = (-screenPosition.y / screenPosition.z) * height/2;

        return new Vec3(screenX, screenY, screenPosition.z);

    }

    public static void renderQuadWithTexture(GuiGraphics guiGraphics, ResourceLocation texture, int size, Color color, Vec3 worldPos, float alpha, boolean shadow){

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || guiGraphics == null) return;

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Vector3f normalizedColor = new Vector3f(color.getRed(), color.getGreen(), color.getBlue()).div(255);
        Vec3 screenPos = worldToScreen(worldPos, guiGraphics);

        if (screenPos == null) return;


        if (shadow){
            RenderSystem.setShaderColor(normalizedColor.x / 2.5f, normalizedColor.y/ 2.5f, normalizedColor.z/ 2.5f, alpha);
            int small = (int) (size - (30/100f * size));
            guiGraphics.blit(
                    texture,
                    (int) screenPos.x + width / 2 - small / 2,
                    (int) screenPos.y + height / 2 - small / 2,
                    0,
                    0,
                    small,
                    small,
                    small,
                    small);
        }

        RenderSystem.setShaderColor(normalizedColor.x, normalizedColor.y, normalizedColor.z, alpha);

        guiGraphics.blit(
                texture,
                (int) screenPos.x + width / 2 - size / 2,
                (int) screenPos.y + height / 2 - size / 2,
                0,
                0,
                size,
                size,
                size,
                size);


        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);

        RenderSystem.disableBlend();

    }

    public static void renderQuadInBetweenEntities(Entity entity1, Entity entity2, PoseStack poseStack, Color color, MultiBufferSource bufferSource) {
        //This doesnt work and i dont know why
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float stripSize = 0.5f;

        Vec3 pos1 = entity1.position();
        Vec3 pos2 = entity2.position();

        //shamelessly copypasting code from affix, hehehehe

        Vec3 dir = pos2.subtract(pos1).normalize();
        Vec3 cameraLookVec = new Vec3(camera.getUpVector());
        Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(stripSize / 2);
        Vec3 side2 = side1.scale(-1);

        Vec3 v1 = pos1.add(side1);
        Vec3 v2 = pos1.add(side2);
        Vec3 v3 = pos2.add(side2);
        Vec3 v4 = pos2.add(side1);

        buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(poseStack.last().pose(), (float) v3.x, (float) (v3.y + 1.2), (float) v3.z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        buffer.addVertex(poseStack.last().pose(), (float) v4.x, (float) (v4.y + 1.2), (float) v4.z).setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.disableDepthTest();
        RenderSystem.enableCull();
    }


    private static float screenShakeStrength = 0;
    private static float screenShakeTimer = 0;



    public static void shakeCamera(float screenShakeStrength, float screenShakeTimer){
        ScreenHelpers.screenShakeStrength = screenShakeStrength;
        ScreenHelpers.screenShakeTimer = screenShakeTimer;
    }
    private static final Random RANDOM = new Random();
    public static Vec3 getShakedOffset(){
        Vec3 shakedOffset;

        float rx = (RANDOM.nextFloat() -.5f) * 2 * screenShakeStrength;
        float ry = (RANDOM.nextFloat() -.5f) * 2 * screenShakeStrength;
        float rz = (RANDOM.nextFloat() -.5f) * 2 * screenShakeStrength;

        shakedOffset = new Vec3(rx, ry, rz);
        if (screenShakeTimer > 0){
            screenShakeTimer--;
            if (screenShakeTimer < 0) screenShakeTimer = 0;
        }

        if (screenShakeTimer == 0) screenShakeStrength = 0;

        return shakedOffset;

    }

}
